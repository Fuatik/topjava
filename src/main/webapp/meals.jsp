<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <form method="get" id="filter">
        <input type="hidden" name="action" value="filter">
        <div class="row">
            <div class="col-2">
                <label for="startDate">От даты (включая)</label>
                <input type="date" class="form-control" name="startDate" id="startDate" value="${param.startDate}">
            </div>
            <div class="col-2">
                <label for="endDate">До даты (включая)</label>
                <input type="date" class="form-control" name="endDate" id="endDate" value="${param.endDate}">
            </div>
            <div class="offset-2 col-3">
                <label for="startTime">От времени (включая)</label>
                <input type="time" class="form-control" name="startTime" id="startTime" value="${param.startTime}">
            </div>
            <div class="col-3">
                <label for="endTime">До времени (исключая)</label>
                <input type="time" class="form-control" name="endTime" id="endTime" value="${param.endTime}">
            </div>
        </div>
            <button type="submit">
                Отфильтровать
            </button>
        </form>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>