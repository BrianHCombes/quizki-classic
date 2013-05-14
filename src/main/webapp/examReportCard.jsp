<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Exam Report Card Graded - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "Exam Report Card");
	request.getSession().setAttribute(Constants.SHOULD_ALLOW_QUESTION_EDITING, false);
	</jsp:scriptlet>
	
<br/><br/><br/>
	Okay, you correctly answered ${numberOfQuestionsAnsweredCorrectly} out of ${totalNumberOfQuestions} questions.<br/><br/>
	
	Questions in <span class="greenText">green</span> are correct, <span class="redText">red</span> is one you missed!<br/><br/>
	
	<c:forEach var="answeredQuestion" items="${currentExamHistory.iterator}">
		<div class="qIsCorrect_${answeredQuestion.isCorrect}">
			<c:choose>
			<c:when test="${empty answeredQuestion.question.description}">
				<a class="q_aIsCorrect_${answeredQuestion.isCorrect}" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.text}</a>
			</c:when>
			<c:otherwise>
				<a class="q_aIsCorrect_${answeredQuestion.isCorrect}" href="/displayQuestion.jsp?questionId=${answeredQuestion.question.id}">${answeredQuestion.question.description}</a>
			</c:otherwise>
			</c:choose>
		</div>
	</c:forEach>
	<br/><br/>
	<form action="/ExamReportCardServlet" method="post">
	Give some feedback to the owner of this exam!
	<br/><br/>
	<textarea id="id_examFeedbackTextarea" name="examFeedbackTextarea" cols="50" rows="15" value="Enter some feedback"/>
	<br/>
	<input type="submit" value="Send Feedback" name="button"/>
	</form>
	
	
	<br/><br/><br/>
	<a href="/index.jsp">home</a>
	
</body>
</html>
</jsp:root>