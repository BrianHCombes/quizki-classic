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
		<title>Insert title here</title>
		<link href="css/smoothness/jquery-ui-1.9.2.custom.css" rel="stylesheet" type="text/css"/>
		<link href="css/questions.css" rel="stylesheet" type="text/css"/>
		
		<jsp:text>
			<![CDATA[ <script src="/js/jquery-1.8.2.min.js" type="text/javascript"></script> ]]>
			<![CDATA[ <script src="js/createQuestion.js" type="text/javascript" ></script> ]]>
		</jsp:text>
				
	</head>
<body>

<jsp:include page="header.jsp"></jsp:include>


	<form action="/TakeExamServlet" method="post">
	<br/><br/><br/><br/>
	You've completed this exam!<br/><br/>
	You can go back and review/change your answers, or click <input type="submit" value="GRADE IT!" name="button"/> to see how you did!<br/><br/>
	
		<input type="submit" value="&lt; PREV" name="button"/>

	</form>
	
	<br/><br/>
	<a href="/index.jsp">home</a>
	
	<div class="hidden" id="radioButtonExample"><input type="radio" name="group1" value="??2" />??1<br/></div>	
	<div class="hidden" id="checkboxExample"><input type="checkbox" name="??2" value="??2" />??1<br/></div>
</body>
</html>
</jsp:root>