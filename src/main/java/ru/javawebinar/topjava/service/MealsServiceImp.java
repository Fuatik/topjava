package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.memory.MemoryMealsDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

public class MealsServiceImp implements MealsService {
    private final MemoryMealsDao mealsDao;

    public MealsServiceImp(MemoryMealsDao mealsDao) {
        this.mealsDao = mealsDao;
    }

    @Override
    public void create(Meal meal) {
        mealsDao.create(meal);
    }

    @Override
    public List<MealTo> readAll() {
        return MealsUtil.filteredByStreams(mealsDao.readAll(), LocalTime.MIN, LocalTime.MAX, MemoryMealsDao.caloriesPerDay);
    }

    @Override
    public Meal read(Integer id) {
        return mealsDao.read(id);
    }

    @Override
    public void update(Meal meal) {
        mealsDao.update(meal);
    }

    @Override
    public void delete(Integer id) {
        mealsDao.delete(id);
    }
}
