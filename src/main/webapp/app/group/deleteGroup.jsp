<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../../fragments/head-section.jsp" %>
    <title>School - deleting users group</title>
</head>
<body>
<%@include file="../../fragments/header.jsp" %>
<div class="container">
    <div class="distancer"></div>
    <p>You are about to delete this group</p>
    <p>
        group id: ${group.id}<br>
        group name: ${group.name}
    </p>
    <p><span class="bold">Are you sure?</span></p>
    <form method="post" action="/app/group/delete">
        <input type="number" value="${group.id}" name="id" hidden>
        <input type="submit" class="button grey" value="Yes"/>
        <a href="/app/group/groups" class="button grey">No</a>
    </form>


</div>
<%@include file="../../fragments/footer.jsp" %>
</body>
</html>
