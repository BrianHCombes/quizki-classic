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


function Exams_getHeadDOMElementInOriginalHeader() {
	return $("#belowTheBarPageHeader");
}

function Exams_getHeadDOMElementInClonedHeader() {
	return $("div#header-fixed");
}

$("#containsFilter").keypress(function( event ) {
	if (event.which == 13) {
		applyFilter();
	}
});

$("#searchQuestionsBtn").click(function() {
	applyFilter();
});

$("#topicContainsFilter").keypress(function() {
	if (event.which == 13) {
		applyFilter();
	}
});

$("#searchTopicsBtn").click(function() {
	applyFilter();
});

$("#questionTypeFilter").change(function() {
	applyFilter();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#difficultyFilter").change(function() {
	applyFilter();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#rangeOfQuestionsFilter").change(function() {
	applyFilter();

	// the point here is to move the focus elsewhere than this component.
	//  This is because when it maintains focus, it is not hidden when the 
	//  user scrolls, so it ruins the effect of the hidden fixed header at the top.
	setFocusOnTheContainer();	
});

$("#idApplyFilterButton").click(function() {
	applyFilter();
});

$("#idClearFilterButton").click(function() {
	Exams_resetFilters();
	applyFilter();
});

function applyFilter() {
	clearNoMoreItemsToDisplayFlag();
	getQuestions();
}

function Exams_getJSONFromServerSuppliedData(parsedJSONObject) {
	return parsedJSONObject.question;
}

function getQuestions() {
	setRowsOffsetToZero();
	Exams_cleanTable();
	displayMoreRows(addCheckboxToRow);
}

//called by smooth-scrolling.js::displayMoreRows when some anonymous piece of code calls displayMoreRows, as opposed to
//an invocation that happened in this file (see getQuestions()).
function Exams_thisFunctionCalledForEachRowByDisplayMoreRows(row) {
	addCheckboxToRow(row);
}

function Exams_getEntities() {
	getQuestions();
}

function Exams_cleanTable() {
    $("#examEntityTableRows tbody tr:not(.filter-row)").remove();
    clearNoMoreItemsToDisplayFlag();
}

function Exams_getNoItemsFoundHTMLString(string) {
	var rtn = "";
	
	rtn += '<tr class="table-status-row"></tr>';
	rtn += '<tr class="table-status-row" id="tableRow_0">';
	rtn += '<td>';
	rtn += '</td><td colspan="6">' + string + '</td>';
	rtn += '</tr>';
	
	return rtn;
}

function Exams_convertToHTMLString(obj, rowNum) {
	var topicsArr = obj.topics;
	
	var rtn = "";
	
	rtn += '<tr id="tableRow_' + rowNum + '">';
	rtn += '<td>';
	
	if (isSelectedEntityId(obj.id) == true) {
		// Checked
		rtn += '<label class="checkbox no-label checked" for="chkbox_' + rowNum + '">';
		rtn += ' <input type="checkbox" class="selectQuestionChkbox" value="" data-toggle="checkbox" id="chkbox_' + rowNum + '"';
		rtn += ' name="selectQuestionChkbox_' + obj.id + '" />';
		rtn += '</label>';
	}
	else {
		// Not checked
		rtn += '<label class="checkbox no-label" for="chkbox_' + rowNum + '">';
		rtn += ' <input type="checkbox" class="selectQuestionChkbox" value="" data-toggle="checkbox" id="chkbox_' + rowNum + '"';
		rtn += ' name="selectQuestionChkbox_' + obj.id + '" />';
		rtn += '</label>';
	}
	
	rtn += '</td><td class="examTableQuestionColumn">';
	
	if (obj.description.length > 0) {
		rtn += '<a href="/displayQuestion.jsp?questionId=' + obj.id + '">' + obj.description + '</a>';
	}
	else {
		rtn += '<a href="/displayQuestion.jsp?questionId=' + obj.id + '">' + obj.textWithoutHTML + '</a>';
	}
	
	rtn += '</td><td class="examTableTopicsColumn">';
	
	if (topicsArr.length > 0) {
		for (var i=0; i<topicsArr.length; i++) {
			rtn += topicsArr[i].text + '<br/>';
		}
	}
	
	rtn += '</td><td>';
	
	rtn += obj.type_text;
	rtn += '</td><td>';
	rtn += obj.difficulty_text;
	rtn += '</td><td>';
	
	// TODO: figure out a way of populating the Vote info.. Probably put it in a JSON str, like [{"objectId":"1","votesUp":"1","votesDown":"0"}]
	//  then create a map of some sort out of it..
//	rtn += ' -- ';
	rtn += '</td><td>';
	
	return rtn;
}

function Exams_setPersistEntityDataObjectDefinition() {
	var str = '{"fields": [{"name":"examTitle","id":"#id_examTitle"},{"name":"examMessage","id":"#id_examMessage"}]}';
	
	$('#Exams-persist-entity-dataObjectDefinition').attr('value', str);
}

function Exams_resetFilters() {
	$("#containsFilter").attr("value", "");
	$("#topicContainsFilter").attr("value", "");

	setRangeOfQuestionsFilterToDefaultValue();
	
	$("button#rangeOfQuestionsFilter ~ ul > li.selected").removeClass('selected');
	$("button#rangeOfQuestionsFilter ~ ul > li[rel='0']").addClass('selected');

	$("#questionTypeFilter > option[selected='selected']").removeAttr('selected');
	$("#questionTypeFilter > option[value='0']").attr('selected', 'selected');
	$("#questionTypeFilter > span.filter-option").html($("#questionTypeFilter > option[value='0']").html());
	$("button#questionTypeFilter ~ ul > li.selected").removeClass('selected');
	$("button#questionTypeFilter ~ ul > li[rel='0']").addClass('selected');

	$("#difficultyFilter > option[selected='selected']").removeAttr('selected');
	$("#difficultyFilter > option[value='0']").attr('selected', 'selected');
	$("#difficultyFilter > span.filter-option").html($("#difficultyFilter > option[value='0']").html());
	$("button#difficultyFilter ~ ul > li.selected").removeClass('selected');
	$("button#difficultyFilter ~ ul > li[rel='0']").addClass('selected');
}

function getTheAppropriateArray(parsedJSONObject) {
	return parsedJSONObject.question;
}

function setRangeOfQuestionsFilterToDefaultValue() {
	setRangeOfQuestionsFilterValue("1");
}

function setRangeOfQuestionsFilterValue(val) {
	$("#rangeOfQuestionsFilter > option[selected='selected']").removeAttr('selected');
	$("#rangeOfQuestionsFilter > option[value='"+val+"']").attr('selected', 'selected');
	$("#rangeOfQuestionsFilter > span.filter-option").html($("#rangeOfQuestionsFilter > option[value='"+val+"']").html());
}