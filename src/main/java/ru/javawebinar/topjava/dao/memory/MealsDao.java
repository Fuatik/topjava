package ru.javawebinar.topjava.dao.memory;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDao {
    Meal save(Meal meal);
    List<Meal> readAll();
    Meal read(int id);
    void delete(Integer id);
}
