package ru.javawebinar.topjava.dao.memory;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealsDao {
    public static final int caloriesPerDay = 2000;
    public static final Map<Integer, Meal> meals;
    public static final AtomicInteger id;

    static {
        meals = new ConcurrentHashMap<>();
        meals.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        id = new AtomicInteger(meals.size() + 1);
    }

    public Meal create(Meal meal) {
        meal.setId(id.getAndIncrement());
        meals.put(meal.getId(), meal);
        return meal;
    }

    public List<Meal> readAll() {
        List<Meal> result = new ArrayList<>(meals.values());
        result.sort(Comparator.comparing(Meal::getId));
        return result;
    }

    public Meal read(Integer id) {
        return meals.get(id);
    }

    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    public void delete(Integer id) {
        meals.remove(id);
    }
}
