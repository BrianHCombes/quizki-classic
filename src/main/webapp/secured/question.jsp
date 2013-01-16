<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
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
		<title>Create Question</title>
		<link href="../css/smoothness/jquery-ui-1.8.24.custom.css" rel="stylesheet" type="text/css"/>
		<link href="../css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="../js/createQuestion.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="../js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
tinyMCE.init({
        mode : "textareas",
		content_css : "../css/custom_content.css",
		theme_advanced_font_sizes: "10px,12px,13px,14px,16px,18px,20px",
		font_size_style_values : "10px,12px,13px,14px,16px,18px,20px",
        theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyfull,|,formatselect",
        theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,image,|,hr,removeformat,visualaid,|,sub,sup,|,charmap",
        theme_advanced_buttons3 : "",
		theme_advanced_path : false        
});
</script>
]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="../header.jsp"></jsp:include>

	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<h1>Create Question</h1>
	</c:when>
	<c:otherwise>
	<h1>Edit Question</h1>
	</c:otherwise>
	</c:choose>

      <c:if test="${not empty requestScope.successes}">
      	<c:forEach var="str" items="${requestScope.successes}">
      		<span class="greenText">${str}</span><br/>	
      	</c:forEach>
      	<br/>
      </c:if>
      
      <c:if test="${not empty requestScope.validationErrors}">
      	<c:forEach var="str" items="${requestScope.validationErrors}">
      		<span class="redText">${str}</span><br/>
      	</c:forEach>
      	<br/>      	
      </c:if>

	<c:choose>
		<c:when test="${empty requestScope.doNotAllowEntityEditing}">
	
		<div >
		<form action="/secured/QuestionServlet" method="post">
			<div >
			Description: <input type="text" size="45" name="questionDescription" value="${currentQuestion.description}"/><br/><br/>
			<textarea name="questionText" cols="50" rows="15" value="Enter a question...?">${currentQuestion.text}</textarea><br/>  
			Difficulty: <select name="difficulty">
				<c:choose><c:when test="${currentQuestion.difficulty.id == 1}"><option value="junior" selected="selected">Junior</option></c:when><c:otherwise><option value="junior" >Junior</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 2}"><option value="intermediate" selected="selected">Intermediate</option></c:when><c:otherwise><option value="intermediate" >Intermediate</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 3}"><option value="wellversed" selected="selected">Well-versed</option></c:when><c:otherwise><option value="wellversed" >Well-versed</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.difficulty.id == 4}"><option value="guru" selected="selected">Guru</option></c:when><c:otherwise><option value="guru" >Guru</option></c:otherwise></c:choose>
				</select> | Type: <select name="type">
				<c:choose><c:when test="${currentQuestion.questionType.id == 1}"><option value="Single" selected="selected">Single</option></c:when><c:otherwise><option value="Single">Single</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 2}"><option value="Multi" selected="selected">Multi</option></c:when><c:otherwise><option value="Multi">Multi</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 3}"><option value="Single" selected="selected" disabled="disabled">String</option></c:when><c:otherwise><option value="String" disabled="disabled">String</option></c:otherwise></c:choose>
				<c:choose><c:when test="${currentQuestion.questionType.id == 4}"><option value="Single" selected="selected" disabled="disabled">Sequence</option></c:when><c:otherwise><option value="Sequence" disabled="disabled">Sequence</option></c:otherwise></c:choose>
				</select>	
			</div>
			
			<hr/>
			<br/>
			<div >
			Choices --<br/>
			Choice Text: <input type="text" name="choiceText"/>  Is Correct?: <select name="isCorrect"><option value="no">No</option><option value="yes">Yes</option></select> <input type="submit" value="Add Choice" name="button"/> <input type="submit" value="Add True/False" name="button"/>
			<br/>
			<table>
				<c:forEach var="choice" items="${currentQuestion.choices}">
					<tr>
						<td><input type="text" name="choiceText_${choice.id}" value="${choice.text}"/></td>
						<td>Is Correct? <c:if test="${choice.iscorrect == 1}"><input type="radio" name="group1_${choice.id}" value="Yes" checked="checked" />Yes </c:if> 
										<c:if test="${choice.iscorrect == 0}"><input type="radio" name="group1_${choice.id}" value="Yes" />Yes </c:if>
										<c:if test="${choice.iscorrect == 1}"><input type="radio" name="group1_${choice.id}" value="No" />No </c:if>
										<c:if test="${choice.iscorrect == 0}"><input type="radio" name="group1_${choice.id}" value="No" checked="checked" />No </c:if>
						</td>
						<td><input type="submit" value="Update" name="choiceButton_${choice.id}"/></td>
						<td><input type="submit" value="Delete" name="choiceButton_${choice.id}"/></td>
					</tr>
				</c:forEach>
			</table>
			</div>
			<br/>
			<hr/>
			<div >
			Topics --<br/>
			Topic Text: <input type="text" name="topicText"/>  <input type="submit" value="Add Topic" name="button"/>
			<br/>		
			<table>
				<c:forEach var="topic" items="${currentQuestion.topics}">
					<tr>
						<td><input type="text" name="topicText_${topic.id}" value="${topic.text}" readonly="readonly"/></td>
						<td><input type="submit" value="Delete" name="topicButton_${topic.id}"/></td>
					</tr>
				</c:forEach>
			</table>
			</div>
			
			<br/>
			<br/>
			<hr/>
	<c:choose>
	<c:when test="${empty sessionScope.inEditingMode}">
	<input type="submit" value="Add Question" name="button" style="float:right;"/>
	</c:when>
	<c:otherwise>
	<input type="submit" value="Update Question" name="button" style="float:right;"/>
	</c:otherwise>
	</c:choose>
				
		</form>
		</div>
		
		</c:when>
		<c:otherwise>
			<br/>
			There was an error loading this page. This entity cannot be edited!<br/>
		</c:otherwise>
	</c:choose>
	
	<br/><br/>
	<a href="/index.jsp">home</a>  ---  <a href="listQuestions.jsp">List All Questions</a>
	

</body>
</html>
</jsp:root>