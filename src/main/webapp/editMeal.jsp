<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>EditMeal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>Edit Meal</h3>
<form method="POST" name="edit">
    DateTime:
    <input type="datetime-local"
           name="dateTime"
           value="<c:out value="${meal.dateTime}"/>"/> <br/>
    <br/>
    Description:
    <input type="text" size="30"
           name="description"
           value="<c:out value="${meal.description}"/>"/> <br/>
    <br/>
    Calories:
    <input type="text"
           name="calories"
           value="<c:out value="${meal.calories}"/>"/> <br/>
    <br/>
    <p>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </p>
</form>
</body>
</html>