package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.memory.MemoryMealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.service.MealsServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private final MealsService service;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public MealServlet() {
        super();
        this.service = new MealsServiceImp(new MemoryMealsDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            forward = LIST_MEALS;
            request.setAttribute("mealsTo", service.readAll());
        } else if (action.equalsIgnoreCase("delete")) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            service.delete(id);
            forward = LIST_MEALS;
            request.setAttribute("mealsTo", service.readAll());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            String mealId = request.getParameter("id");
            if (mealId != null) {
                Integer id = Integer.parseInt(mealId);
                Meal meal = service.read(id);
                request.setAttribute("meal", meal);
            }
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to editMeals");
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(null, null, null, 0);
        String mealId = request.getParameter("id");
        if (mealId != null) {
            meal.setId(Integer.valueOf(mealId));
        }
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime"), formatter));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            service.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            service.update(meal);
        }
        request.setAttribute("mealsTo", service.readAll());
        request.getRequestDispatcher(LIST_MEALS).forward(request, response);
    }
}
