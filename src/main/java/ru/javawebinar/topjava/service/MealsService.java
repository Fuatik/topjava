package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.memory.MemoryMealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

public class MealsService {
    private final MemoryMealsDao mealsDao;

    public MealsService(MemoryMealsDao memoryMealsDao) {
        this.mealsDao = memoryMealsDao;
    }

    public void save(Meal meal) {
        mealsDao.save(meal);
    }

    public List<MealTo> readAll() {
        return MealsUtil.filteredByStreams(mealsDao.readAll(), LocalTime.MIN, LocalTime.MAX, 2000);
    }

    public Meal read(Integer id) {
        return mealsDao.read(id);
    }

    public void delete(Integer id) {
        mealsDao.delete(id);
    }
}
