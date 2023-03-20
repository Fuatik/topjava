package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;


@Controller
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService mealService;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        log.info("meals");
        model.addAttribute("meals",
                MealsUtil.getTos(mealService.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("meals/filter")
    public ModelAndView getBetween(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        final List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDate, endDate, userId);
        ModelAndView mav = new ModelAndView("meals");
        mav.addObject("meals",
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        return mav;
    }

    @PostMapping("/meals/create")
    public String create(Map<String, Object> model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("create {} for user {}", meal, SecurityUtil.authUserId());
        model.put("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals/{id}/update")
    public ModelAndView update(@PathVariable String id) {
        int userId = SecurityUtil.authUserId();
        ModelAndView mav = new ModelAndView("mealForm");
        final Meal meal = mealService.get(Integer.parseInt(id), userId);
        log.info("update {} for user {}", meal, userId);
        mav.addObject("meal", meal);
        return mav;
    }

    @PostMapping("/meals/{id}/delete")
    public String delete(@PathVariable String id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", Integer.parseInt(id), userId);
        mealService.delete(Integer.parseInt(id), userId);
        return "redirect:/meals";
    }

    @PostMapping("/meals/save")
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
            meal.setId(id);
            mealService.update(meal, userId);
        } else {
            mealService.create(meal, userId);
        }
        return "redirect:/meals";
    }
}
