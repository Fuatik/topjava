package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private final int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("filter");
        if (startDate.equals("")) startDate = String.valueOf(LocalDate.MIN);
        if (endDate.equals("")) endDate = String.valueOf(LocalDate.MAX);
        if (startTime.equals("")) startTime = String.valueOf(LocalTime.MIN.truncatedTo(ChronoUnit.MINUTES));
        if (endTime.equals("")) endTime = String.valueOf(LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES));
        return MealsUtil.getFilteredTos(service.getFiltered(authUserId(), LocalDate.parse(startDate), LocalDate.parse(endDate)),
                caloriesPerDay, LocalTime.parse(startTime), LocalTime.parse(endTime));
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), caloriesPerDay);
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("Create {}", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("Delete id={}", id);
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("Update {}", meal);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}