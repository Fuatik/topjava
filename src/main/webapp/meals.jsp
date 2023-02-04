<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="index.html">Add Meal</a></h4>
<table border="1">
    <tr><td><B>Date</B></td><td><B>Description</B></td><td><B>Calories</B></td><td></td><td></td></tr>
    <c:forEach var="meal" items="${mealsTo}">
    <p><tr style="color: ${meal.excess ? 'red' : 'green'}"><td>${meal.textDateTime}</td><td>${meal.description}
    </td><td>${meal.calories}</td><td><a href="index.html">Update</a></td><td><a href="index.html">Delete</a></td></tr><p>
    </c:forEach>
</table>
</body>
</html>