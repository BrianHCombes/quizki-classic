package com.haxwell.apps.questions.entities;

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.haxwell.apps.questions.constants.DifficultyEnums;
import com.haxwell.apps.questions.entities.Difficulty;
import com.haxwell.apps.questions.utils.StringUtil;

@Category(com.haxwell.apps.questions.testTypes.UnitTests.class)
public class DifficultyTest {

	@Test
	public void testObjectDefaultConstructor() {
		Difficulty sut = new Difficulty();
		assertTrue(StringUtil.isNullOrEmpty(sut.getText()));
		assertTrue(sut.getId() == 0);
	}
	
	@Test
	public void testObjectStringConstructor() {
		Difficulty sut = new Difficulty(DifficultyEnums.GURU.getValString());
		assertTrue(StringUtil.equals(sut.getText(), DifficultyEnums.GURU.getValString()));
	}
	
	@Test
	public void testObjectLongConstructor() {
		Difficulty sut = new Difficulty(DifficultyEnums.GURU.getRank());
		assertTrue(sut.getText() == null);
		assertTrue(sut.getId() == DifficultyEnums.GURU.getRank());
	}
	
	@Test
	public void testObjectStringLongConstructor() {
		Difficulty sut = new Difficulty(DifficultyEnums.GURU.getValString(), DifficultyEnums.GURU.getRank());
		assertTrue(StringUtil.equals(sut.getText(), DifficultyEnums.GURU.getValString()));
		assertTrue(sut.getId() == DifficultyEnums.GURU.getRank());
	}

	@Test
	public void testSettersAndGetters() {
		Difficulty sut = new Difficulty(DifficultyEnums.GURU.getValString(), DifficultyEnums.GURU.getRank());
		
		sut.setText(DifficultyEnums.SENIOR.getValString());
		sut.setId(DifficultyEnums.INTERMEDIATE.getRank());
		
		assertTrue(StringUtil.equals(sut.getText(), DifficultyEnums.SENIOR.getValString()));
		assertTrue(sut.getId() == DifficultyEnums.INTERMEDIATE.getRank());
	}
	
	@Test
	public void testEquals() {
		Difficulty sut1 = new Difficulty();
		Difficulty sut2 = new Difficulty();
		
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		
		sut1.setId(1);
		
		assertFalse(sut1.equals(sut2));

		sut2.setId(1);
		
		sut1.setText("text");
		
		assertFalse(sut1.equals(sut2));
		
		sut2.setText("text");
		
		assertTrue(sut1.equals(sut2));
	}
}
