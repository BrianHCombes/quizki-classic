package com.haxwell.apps.questions.checkers;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.util.List;
import java.util.Map;

import com.haxwell.apps.questions.entities.Choice;
import com.haxwell.apps.questions.entities.Question;
import com.haxwell.apps.questions.utils.QuestionUtil;

public class SequenceQuestionTypeChecker extends AbstractQuestionTypeChecker {

	public SequenceQuestionTypeChecker(Question q) {
		super(q);
	}
		
	@Override
	public boolean questionIsCorrect(Map<String, String> mapOfFieldNamesToValues)
	{
		List<Choice> choices = QuestionUtil.getChoiceList(this.question);
		
		boolean rtn = true;

		for (Choice c : choices)
		{
			String userSuppliedValue = mapOfFieldNamesToValues.get(this.question.getId() + "," + c.getId());
			rtn &= (userSuppliedValue != null && userSuppliedValue.equals(c.getSequence()+""));
		}
		
		return rtn;
	}

}
