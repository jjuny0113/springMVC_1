<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
  <li>id=${member.id}</li>
  <li>username=${member.username}</li>
  <li>age=${member.age}</li>
<%--  ((Member)request.getAttribute("member")).getAge()--%>
</ul>
<a href="${pageContext.request.contextPath}/index.html">메인</a>
</body>
</html>
