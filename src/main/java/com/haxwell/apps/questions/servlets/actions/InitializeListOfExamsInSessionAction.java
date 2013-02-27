package com.haxwell.apps.questions.servlets.actions;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.haxwell.apps.questions.constants.Constants;
import com.haxwell.apps.questions.entities.Exam;
import com.haxwell.apps.questions.entities.User;
import com.haxwell.apps.questions.managers.ExamManager;

/**
 * Ensures that the list of exams in the session is the most up to date it can be.
 * 
 * @author johnathanj
 */
public class InitializeListOfExamsInSessionAction implements AbstractServletAction {

	Logger log = Logger.getLogger(InitializeListOfExamsInSessionAction.class.getName());
	
	@Override
	public int doAction(ServletRequest request, ServletResponse response) {
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest req = ((HttpServletRequest)request);
			
			if (req.getSession().getAttribute(Constants.LIST_OF_EXAMS_TO_BE_DISPLAYED) == null) {
				
				User user = (User)req.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
				Collection<Exam> coll = null;
	
				if (user == null ) 
					coll = ExamManager.getAllExams();
				else 
					coll = ExamManager.getAllExamsForUser(user.getId());
				
				req.getSession().setAttribute("fa_listofexamstobedisplayed", coll);
				
				log.log(Level.INFO, "Just added " + coll.size() + " exams to the fa_listofexamstobedisplayed list");
		}}
		
		return 0;
	}
}

