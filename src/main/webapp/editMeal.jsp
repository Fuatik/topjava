<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>EditMeal</title>
</head>
<body>
<a href="index.html">Home</a>
<hr>
<h3>Edit Meal</h3>
<form method="POST" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    DateTime:
    <input type="datetime-local"
           name="dateTime"
           value="${meal.dateTime}"/> <br/>
    <br/>
    Description:
    <input type="text" size="30"
           name="description"
           value="${meal.description}"/> <br/>
    <br/>
    Calories:
    <input type="text"
           name="calories"
           value="${meal.calories}"/> <br/>
    <br/>
    <p>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </p>
</form>
</body>
</html>