package com.haxwell.apps.questions.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.EventConstants;
import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.events.EventDispatcher;
import com.haxwell.apps.questions.factories.QuestionTypeCheckerFactory;
import com.haxwell.apps.questions.managers.Manager;
import com.haxwell.apps.questions.managers.QuestionManager;

public class QuestionUtil {

	public static String getFieldnameForChoice(Question q, Choice c)
	{
		return "field_" + "question" + q.getId() + "choice" + c.getId();
	}
	
	public static List<String> getFieldnamesForChoices(Question q)
	{
		List<Choice> choices = getChoiceList(q);
		
		LinkedList<String> list = new LinkedList<String>();
		
		for (Choice c: choices)	{
			list.add(getFieldnameForChoice(q, c));
		}
		
		return list;
	}
	
	public static Collection<String> getFieldnamesForCorrectChoices(Question q) {
		List<Choice> choices = getChoiceList(q);
		
		LinkedList<String> list = new LinkedList<String>();
		
		for (Choice c: choices)	{
			if (c.getIsCorrect() > 0)
				list.add(getFieldnameForChoice(q, c));
		}
		
		return list;
	}
	
	/**
	 * Lists of choices are always ordered by ID. A question could have 20 choices, and
	 * in any list of items related to those 20 choices, the first item in the list relates
	 * to the choice with the highest ID. The second item, the second highest ID, etc.
	 *   
	 * @param currentQuestion
	 * @return
	 */
	public static List<Choice> getChoiceList(Question currentQuestion) {
		Set<Choice> choices = currentQuestion.getChoices();
		
		ArrayList<Choice> list = new ArrayList<Choice>(choices);
		
		Collections.sort(list, Manager.ID_COMPARATOR);
		
		return list;
	}
	
	// ..except for when they're not.. :)
	public static List<Choice> getChoiceListBySequenceNumber(Question currentQuestion) {
		Set<Choice> choices = currentQuestion.getChoices();
		
		ArrayList<Choice> list = new ArrayList<Choice>(choices);
		
		Collections.sort(list, Manager.SEQUENCE_NUMBER_COMPARATOR);
		
		return list;
	}
	
	/**
	 * 
	 */
	public static Map<String, List<String>> persistQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = buildQuestionBasedOnHttpServletRequest(request);
		
		Map<String, List<String>> rtn = new HashMap<String, List<String>>();
		
		List<String> errors = QuestionManager.validate(question);
		
		if (errors.size() == 0)
		{
			long qid = QuestionManager.persistQuestion(question);
			List<String> successes = new ArrayList<String>();
			
			successes.add("Question was successfully saved! ");
			
			rtn.put("successes", successes);			

//			request.getSession().setAttribute(Constants.CURRENT_QUESTION, null);
			request.setAttribute(Constants.CURRENT_QUESTION, null);
			
//			request.getSession().setAttribute(Constants.URL_TO_REDIRECT_TO_WHEN_BACK_BUTTON_PRESSED, "/secured/question.jsp");
			request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Edit Question");
//			request.getSession().setAttribute(Constants.CURRENT_QUESTION_HAS_BEEN_PERSISTED, Boolean.TRUE);
//			request.getSession().setAttribute(Constants.IN_EDITING_MODE, null); // HACK!! I would rather do this in the initializeQuestions filter, but its not being called by the forwardToJSP() call.
			request.setAttribute(Constants.SUCCESS_MESSAGES, successes);
			
			EventDispatcher.getInstance().fireEvent(request, EventConstants.QUESTION_WAS_PERSISTED);
		}
		else
		{
//			log.log(Level.SEVERE, "errors.size() == " + errors.size());
//			request.setAttribute(Constants.VALIDATION_ERRORS, errors);
			rtn.put("errors", errors);
		}

		return rtn;
	}
	
	/**
	 * 
	 */
	public static Question buildQuestionBasedOnHttpServletRequest(HttpServletRequest request) {
		Question question = new Question();
		
		User user = (User)request.getSession().getAttribute("currentUserEntity");

		String questionUserId = (String)request.getParameter("user_id");
		Long user_id = Long.parseLong(questionUserId == null ? user.getId()+"" : questionUserId);

		Object obj = request.getSession().getAttribute(Constants.NEXT_SEQUENCE_NUMBER);
		int nextSequenceNumber = Integer.MIN_VALUE;

		if (obj != null) {
			nextSequenceNumber = Integer.parseInt(obj.toString());
		}

		if (user.getId() == user_id) {
			String id = (String)request.getParameter("id");
			
			if (id != null)
				question.setId(Long.parseLong(id));
			
			question.setUser(user);

			question.setText((String)request.getParameter("text"));
			question.setDescription((String)request.getParameter("description"));
			question.setDifficulty(DifficultyUtil.getDifficulty(request.getParameter("difficulty_id")));
			question.setQuestionType(TypeUtil.getObjectFromStringTypeId(request.getParameter("type_id")));
			question.setChoices(QuestionUtil.getSetFromAjaxDefinition(request.getParameter("choices"), nextSequenceNumber));
			question.setTopics(TopicUtil.getSetFromCSV((String)request.getParameter("topics")));
			question.setReferences(ReferenceUtil.getSetFromCSV((String)request.getParameter("references")));

		}
		
		return question;
	}
	
	/**
	 * Returns a map of the fieldnames to the values chosen for each.
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getChosenAnswers(HttpServletRequest request)
	{
		Logger log = Logger.getLogger(QuestionUtil.class.getName());
		ExamHistory eh = (ExamHistory)request.getSession().getAttribute(Constants.CURRENT_EXAM_HISTORY);
		AbstractQuestionTypeChecker checker = QuestionTypeCheckerFactory.getChecker(eh.getMostRecentlyUsedQuestion());
		
		// take the list of field names from this question, check for that parameter in the request,
		List<String> fieldNames = checker.getKeysToPossibleUserSelectedAttributesInTheRequest(); 
		
		Map<String, String> map = new HashMap<String, String>(); // map fieldnames to the values chosen for them
		
		// now that we have the list of potentially chosen names, which are in the request? which were actually chosen?
		for (String fieldName: fieldNames)
		{
			String str = request.getParameter(fieldName);
			
			//  add the value of this chosen Choice to the list..
			if (str != null)
				map.put(fieldName, str);
		}
		
		log.log(Level.FINER, "Chosen answers: " + StringUtil.getToStringOfEach(map.values()));

		return map;
	}
	
	/**
	 * Given a collection of fieldnames, and a question, returns an array of equal size to the number of choices on the question.
	 * For each element in the resulting array, the word 'true' appears if the corresponding element in the question's list of choices 
	 * appears in the list of fieldnames.
	 * 
	 * @param fieldnamesChosenAsAnswersToGivenQuestion
	 * @param fieldnamesForChoices
	 * @return
	 */
	public static List<String> getUIArray_FieldWasSelected(
			Collection<String> fieldnamesChosenAsAnswersToGivenQuestion,
			Question question) {
		
		if (question == null || fieldnamesChosenAsAnswersToGivenQuestion == null) 
			return new ArrayList<String>();
		
		List<String> rtn = QuestionUtil.getFieldnamesForChoices(question);
		
		for (int i = 0; i < rtn.size(); i++)
		{
				String str = rtn.get(i);
				
				if (fieldnamesChosenAsAnswersToGivenQuestion.contains(str))
					rtn.set(i, "'true'");
				else
					rtn.set(i, "");
		}

		return rtn;
	}
	
	public static String getDisplayString(Question q) {
		return getDisplayString(q, -1);
	}
	
	public static String getDisplayString(Question q, int maxLength) {
		String str = q.getTextWithoutHTML();
		
		if (StringUtil.isNullOrEmpty(str))
			str = q.getDescription();
		
		if (maxLength >= 0)
			maxLength = str.length();
		
		return str.substring(0, maxLength);
	}
	
	public static Set<Choice> getSetFromAjaxDefinition(String str, long newChoiceIndexBegin) {
		Set<Choice> rtn = new HashSet<Choice>();
		
		JSONValue jValue= new JSONValue();
		JSONObject jObj = (JSONObject)jValue.parse(str);
		
		JSONArray arr = (JSONArray)jObj.get("choice");
		
		for (int i=0; i < arr.size(); i++) {
			JSONObject o = (JSONObject)arr.get(i);
			
			Choice c = new Choice();
			
			c.setText((String)o.get("text"));
			c.setSequence(Integer.parseInt((String)o.get("sequence")));
			c.setIscorrect("true".equals((String)o.get("iscorrect")));
			
			Long id = Long.parseLong((String)o.get("id"));
			if (id != -1)
				c.setId(id);
			
			rtn.add(c);
		}
		
		return rtn;
	}
	
}
