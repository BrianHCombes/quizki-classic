package com.haxwell.apps.questions.factories;

import com.haxwell.apps.questions.checkers.AbstractQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.MultiQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.SingleQuestionTypeChecker;
import com.haxwell.apps.questions.checkers.StringQuestionTypeChecker;
import com.haxwell.apps.questions.constants.TypeConstants;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.entities.QuestionType;

public class QuestionTypeCheckerFactory {

	public static AbstractQuestionTypeChecker getChecker(Question q)
	{
		QuestionType qt = q.getQuestionType();
		
		long qtId = qt.getId();
		
		if (qtId == TypeConstants.SINGLE)
			return new SingleQuestionTypeChecker(q);
		else if (qtId == TypeConstants.MULTI)
			return new MultiQuestionTypeChecker(q);
		else if (qtId == TypeConstants.STRING)
			return new StringQuestionTypeChecker(q);
		
		return null;
	}
}
