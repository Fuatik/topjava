package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.memory.MemoryMealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private MealsService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        service = new MealsService(new MemoryMealsDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealsTo", service.readAll());
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("delete")) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            service.delete(id);
            response.sendRedirect("/topjava/meals");
        } else {
                final Meal meal = action.equalsIgnoreCase("edit") ?
                        new Meal(null, "", 1000) :
                        service.read(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to editMeals");
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null :
                Integer.parseInt(id),
                LocalDateTime.parse(request.getParameter("dateTime"), FORMATTER),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        service.save(meal);
        response.sendRedirect("/topjava/meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
