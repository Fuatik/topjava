<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="editMeal?action=edit">Add meal</a></h4>
<table border=1>
    <thead>
    <tr>
        <td><B>Date</B></td>
        <td><B>Description</B></td>
        <td><B>Calories</B></td>
        <th colspan=2>Action</th>
    </tr>
    <c:forEach var="meal" items="${mealsTo}">
    <p>
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>${f:formatLocalDateTime(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="editMeal?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    <p>
        </c:forEach>
</table>
</body>
</html>