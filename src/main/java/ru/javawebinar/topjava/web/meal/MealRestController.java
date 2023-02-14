package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<MealTo> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return service.getAll(authUserId(), startDate, endDate, startTime, endTime);
    }

    public Meal get(int id){
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}