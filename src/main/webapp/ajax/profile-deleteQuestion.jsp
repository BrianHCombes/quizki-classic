<!--
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
 -->

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.managers.QuestionManager"/>
	<jsp:directive.page import="com.haxwell.apps.questions.entities.User"/>
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
	<jsp:directive.page import="com.haxwell.apps.questions.utils.StringUtil"/>
	<jsp:directive.page import="com.haxwell.apps.questions.servlets.actions.SetUserContributedQuestionAndExamCountInSessionAction"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<jsp:scriptlet>

String str = request.getParameter("nameOfLastPressedButton");
String id = (str == null ? null : str.substring(str.indexOf('_')+1));

java.io.PrintWriter writer = response.getWriter();

if (!StringUtil.isNullOrEmpty(id)) {
	
	String btnValue = request.getParameter("valueOfLastPressedButton");
	
	if (btnValue != null) {
		if (btnValue.equals("Delete Question")) {
			User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ENTITY);
			
			String rtn = QuestionManager.deleteQuestion(user.getId(), id);
			
			new SetUserContributedQuestionAndExamCountInSessionAction().doAction(request, response);
			
			writer.print(rtn);
		}
	}
}

</jsp:scriptlet>
	
</body>
</html>
</jsp:root>