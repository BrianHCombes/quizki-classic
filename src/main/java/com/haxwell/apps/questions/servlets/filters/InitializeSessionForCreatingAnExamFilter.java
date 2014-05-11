package com.haxwell.apps.questions.servlets.filters;

import java.io.IOException;	
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.constants.DifficultyConstants;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;
import com.haxwell.apps.questions.utils.CollectionUtil;
import com.haxwell.apps.questions.utils.UserUtil;

/**
 * Makes sure the necessary objects are in the session for the code downstream.
 * 
 */
@WebFilter("/InitializeSessionForCreatingAnExamFilter")
public class InitializeSessionForCreatingAnExamFilter extends AbstractFilter {

	private static Logger log = LogManager.getLogger();
	
	public InitializeSessionForCreatingAnExamFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		log.entry();

		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = ((HttpServletRequest)request);
			HttpSession session = req.getSession();

			String examId = req.getParameter("examId");
			
			if (examId != null) {
				Exam exam = ExamManager.getExam(Integer.parseInt(examId));
				
				User user = (User)session.getAttribute(Constants.CURRENT_USER_ENTITY);
				
				if (user == null || ((exam.getUser().getId() != user.getId()) && !UserUtil.isAdministrator(user))) {
					request.setAttribute("doNotAllowEntityEditing", Boolean.TRUE);
				}
				else {
					session.setAttribute(Constants.CURRENT_EXAM, exam);
					session.setAttribute(Constants.IN_EDITING_MODE, Boolean.TRUE);					
					
					Set<Question> questionSet = exam.getQuestions();
					
					List<Long> selectedQuestionIds = CollectionUtil.getListOfIds(questionSet);
					
					session.setAttribute(Constants.SELECTED_ENTITY_IDS_AS_CSV, CollectionUtil.getCSV(selectedQuestionIds));
					session.setAttribute(Constants.ONLY_SELECTED_QUESTIONS_SHOULD_BE_SHOWN, Boolean.TRUE);
				}
			}
		}
		
		log.exit();
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
