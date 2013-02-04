<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
	<jsp:directive.page import="com.haxwell.apps.questions.constants.Constants"/>
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
    </jsp:text>
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>List Exams - Quizki</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
				
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script> ]]>			
			<![CDATA[ <script src="/js/listQuestions.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
			
				$(function() {
				   $( document ).tooltip();
				 });
					 
			    $( "#open-event" ).tooltip({
			      show: null,
			      position: {
			        my: "left top",
			        at: "left bottom"
			      },
			      open: function( event, ui ) {
			        ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "slow" );
			      }
			    });

		    </script> ]]>
			
		</jsp:text>
	</head>
<body>

	<jsp:scriptlet>      
	request.getSession().setAttribute(Constants.TEXT_TO_DISPLAY_FOR_PREV_PAGE, "List All Questions");
	</jsp:scriptlet>


<jsp:include page="header.jsp"></jsp:include>

		<br/>
		
		<c:choose>
		<c:when test="${empty fa_listofexamstobedisplayed}">
		<br/><br/>You haven't created any exams yet! (<a href="/secured/exam.jsp">Create Exam</a>)
		</c:when>
		<c:otherwise>
		These are the available exams:
		</c:otherwise>
		</c:choose>
		
		<br/><br/>

		<form action="/ListExamsServlet" method="post">
			<div id="center" class="listOfQuestions"  style="overflow:auto; height:225px; width:100%">
			
			<table class="displayExam">
				<thead>
					<th>ID</th>
					<th></th>
					<th>Exam Name</th>
					<th></th>
					<th></th>
				</thead>
				<tbody>
				<c:set var="rowNum" value="0"/>
				<c:forEach var="exam" items="${fa_listofexamstobedisplayed}">
					<c:set var="rowNum" value="${rowNum + 1}" />
					<c:choose><c:when test="${rowNum % 2 == 0}">
					<jsp:text><![CDATA[<tr>]]></jsp:text>
					</c:when>
					<c:otherwise>
					<jsp:text><![CDATA[<tr class="rowHighlight">]]></jsp:text>
					</c:otherwise></c:choose>
					
						<td>${exam.id}</td>
						<td></td>
						<td>${exam.title}</td>
							<td><input type="submit" value="Take Exam" name="examButton_${exam.id}"/></td>
							<c:choose><c:when test="${exam.user.id == currentUserEntity.id}">
								<td><input type="submit" value="Edit Exam" name="examButton_${exam.id}"/></td>
							</c:when>
							<c:otherwise>
								<td><input type="submit" value="Detail Exam" name="examButton_${exam.id}"/></td>
							</c:otherwise>
							</c:choose>
		
					<jsp:text><![CDATA[</tr>]]></jsp:text>
				</c:forEach>
				</tbody>
			</table>
			</div>
		</form>
		<br/>		<br/>

		<form action="/ListExamsServlet" method="post">		
		Show Exams for <input type="radio" name="group1" value="everyone" selected=""/>Everyone or <input type="radio" name="group1" value="mine" selected=""/>The ones I created.
		<br/>
		<input type="submit" value="Refresh The List" name="button"/>
		</form>
		
<br/><br/><br/><br/>
<a href="/index.jsp">home</a><br/>


</body>
</html>
</jsp:root>