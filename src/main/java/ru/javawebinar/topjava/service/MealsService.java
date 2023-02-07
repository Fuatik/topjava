package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealsService {
    void create(Meal meal);

    List<MealTo> readAll();

    Meal read(Integer id);

    void update(Meal meal);

    void delete(Integer id);
}
