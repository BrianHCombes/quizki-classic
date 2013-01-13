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
		<title>Display Question!!!</title>
		<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
			<![CDATA[ <script src="js/tiny_mce/tiny_mce.js" type="text/javascript" ></script> ]]>
			<![CDATA[
			<script type="text/javascript">
tinyMCE.init({
        mode : "textareas",
        readonly : 1,
		content_css : "css/custom_content.css"
});
</script>
]]>
			<![CDATA[
				<script type="text/javascript">
						function foo(htmlExampleName) {
							var fieldNames = ${listOfFieldNamesForTheCurrentQuestionsChoices};
							var values = ${listOfCurrentQuestionsChoicesValues};
							var selected = ${listSayingAnElementIsCheckedOrNot};
							var isCorrectList = ${listSayingWhichChoicesAreCorrect};
							var examHistoryIsPresent = ${booleanExamHistoryIsPresent};

							$('div.choices').html('');
							
							// find the div, and create the html to put in there, for these choices and 
							//  this question type..
							
							for (var counter=0;fieldNames.length>counter;counter++)
							{
								var str = $(htmlExampleName).html();
		
								var b1 = false;
								var b2 = false;
								
								str = str.replace('??1', values[counter]);
								str = str.replace('??2', fieldNames[counter]);
								str = str.replace('??2', fieldNames[counter]);
								
								if (selected !== undefined && selected[counter] !== undefined && selected[counter] == 'true')
								{
									str = str.replace("selected=\"\"", 'checked');
									b1 = true;
								}
								
								if (isCorrectList !== undefined && isCorrectList[counter] !== undefined && isCorrectList[counter] == 'true') 
									b2 = true;

								if (counter%2 == 0) {
									str = str.replace('??4', 'rowHighlight'); 
								} else {
									str = str.replace('??4', '');
								}

								if (examHistoryIsPresent == true) {									
									if (b1 == true && b2 == true) {
										str = str.replace('??3', 'selectedAndCorrect');
									} else if (b1 == true && !b2 == true) {
										str = str.replace('??3', 'selectedButNotCorrect');
									} else if (b1 == false && b2 == true) {
										str = str.replace('??3', 'correctButNotSelected');
									} 
								}
								else {
									if (b2 == true) {
										str = str.replace('??3', 'greenText');
									}
								}
								
								var previous = $('div.choices').html();
								
								previous += str;
								
								$('div.choices').html(previous);
							}
						}
						
			$(document).ready(function() {
				if (${currentQuestion.questionType.id} == 1)
				{
					foo('#radioButtonExample');
				}
				else if (${currentQuestion.questionType.id} == 2)
				{
					foo('#checkboxExample');
				}
			});
				</script>
			
			]]>
		</jsp:text>

				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

	<h1>Display Question</h1>

		<form action=".">
		Creator: ${currentQuestion.user.username}<br/>
		Description: ${currentQuestion.description}<br/>
		<br/>
		Text: <textarea name="questionText" cols="50" rows="15">${currentQuestion.text}</textarea><br/>  
		Type: ${currentQuestion.questionType.text}  <br/>
		Difficulty: ${currentQuestion.difficulty.text} <br/>
		<hr/>
		<br/>
		Choices --<br/>

		<div class="choices" style="margin-left:25px">.</div>

		<hr/>
		<br/>
		Topics --<br/>
		<div style="margin-left:25px">
			<table style="width:100%">
							<c:set var="rowNum" value="0"/>
							<c:forEach var="topic" items="${currentQuestion.topics}">
								<c:set var="rowNum" value="${rowNum + 1}" />
								<c:choose><c:when test="${rowNum % 2 == 0}">
								<jsp:text><![CDATA[<tr style="width:100%">]]></jsp:text>
								</c:when>
								<c:otherwise>
								<jsp:text><![CDATA[<tr class="rowHighlight" style="width:100%">]]></jsp:text>
								</c:otherwise></c:choose>
					
									<td>
										${topic.text}
									</td>
									<jsp:text><![CDATA[</tr>]]></jsp:text>				
							</c:forEach>
			</table>
		</div>
		</form>
		
		<!-- TODO: Add ability to store and display references for a question -->
		
		<br/><br/>
	<a href="/index.jsp">home</a> -- <a href="javascript:history.go(-1)">Go Back to ${textToDisplayForPrevPage}</a>
	
	<div class="hidden" id="radioButtonExample"><div class="??3 ??4"><input type="radio" disabled="disabled" name="group1" value="??2" selected=""/>??1</div></div>	
	<div class="hidden" id="checkboxExample"><div class="??3 ??4"><input type="checkbox" disabled="disabled" name="??2" value="??2" selected=""/>??1</div></div>

</body>
</html>
</jsp:root>