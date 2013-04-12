package com.haxwell.apps.questions.servlets.actions;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.QuestionManager;
import com.haxwell.apps.questions.utils.PaginationData;
import com.haxwell.apps.questions.utils.StringUtil;

/**
 * Ensures that the list of questions in the session is the most up to date it can be.
 * 
 * @author johnathanj
 */
public class InitializeListOfQuestionsInSessionAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeListOfQuestionsInSessionAction.class.getName());
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			
			if (req.getSession().getAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED) == null) {
				
				PaginationData qpd = (PaginationData)req.getSession().getAttribute(Constants.QUESTION_PAGINATION_DATA);

				User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				Collection<Question> coll = null;

				if (user == null && req.getSession().getAttribute(Constants.SHOULD_ALL_QUESTIONS_BE_DISPLAYED) != null) {
					coll = QuestionManager.getAllQuestions(qpd);
					req.getSession().setAttribute(Constants.SHOULD_ALL_QUESTIONS_BE_DISPLAYED, null);					
				}
				else if (user != null) {
					if (req.getSession().getAttribute(Constants.ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN) != null) {
						req.getSession().setAttribute(Constants.ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN, null);
						
						List<Long> selectedQuestionIds = (List<Long>)req.getSession().getAttribute(Constants.CURRENT_EXAM_SELECTED_QUESTION_IDS);
						
						coll = QuestionManager.getQuestionsById(StringUtil.getCSVString(selectedQuestionIds), qpd);
					}
					else 
						coll = QuestionManager.getAllQuestionsForUser(user.getId(), qpd);
				}

				req.getSession().setAttribute(Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED, coll);

				if (coll != null)
					log.log(Level.INFO, "Just set " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + "to have " + coll.size() + " items.");
				else
					log.log(Level.INFO, "coll was null. No changes made to the " + Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " list");
			}
			else
				log.log(Level.INFO, Constants.LIST_OF_QUESTIONS_TO_BE_DISPLAYED + " was not null so ListQuestionsFilter did not reset the list of questions to be displayed.");
		}
		
		return 0;
	}
}

