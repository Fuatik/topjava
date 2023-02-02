package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate)).values()
                .stream().flatMap(daysMeals -> {
                    boolean exceed = daysMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                    return daysMeals.stream().filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                            .map(meal -> createTo(meal, exceed));
                }).collect(Collectors.toList());
        /*return meals.stream()
        .map((e) -> {
            UserMealWithExcess userMealWithExcess;
            if ((meals.stream()
                    .filter(s -> s.getDateTime().toLocalDate().equals(e.getDateTime().toLocalDate()))
                    .map(UserMeal::getCalories))
                    .mapToInt(Integer::intValue)
                    .sum() > caloriesPerDay) {
                userMealWithExcess = new UserMealWithExcess(e.getDateTime(),
                        e.getDescription(), e.getCalories(), true);
            } else {
                userMealWithExcess = new UserMealWithExcess(e.getDateTime(),
                        e.getDescription(), e.getCalories(), false);
            }
            return userMealWithExcess;
        })
        .filter((e) -> TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime))
        .collect(Collectors.toList());*/
    }

    public static List<MealTo> filteredByStreamsWithOwnCollector(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class Aggregate {
            private final List<Meal> dailyMeals = new ArrayList<>();
            private int dailySumOfCalories;

            private void accmulate(Meal meal) {
                dailySumOfCalories += meal.getCalories();
                if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                    dailyMeals.add(meal);
                }
            }

            private Aggregate combine(Aggregate that) {
                this.dailySumOfCalories += that.dailySumOfCalories;
                this.dailyMeals.addAll(that.dailyMeals);
                return this;
            }

            private Stream<MealTo> finisher() {
                final boolean exceed = dailySumOfCalories > caloriesPerDay;
                return meals.stream().map(meal -> createTo(meal, exceed));
            }
        }
        Collection<Stream<MealTo>> values = meals.stream().collect(Collectors.groupingBy(Meal::getDate,
                Collector.of(Aggregate::new, Aggregate::accmulate, Aggregate::combine, Aggregate::finisher)))
                .values();
        return values.stream().flatMap(x -> x).collect(Collectors.toList());
    }

    public static List<MealTo> filteredByRecursion(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<MealTo> result = new ArrayList<>();
        filterWithRecursion(new LinkedList<>(meals), startTime, endTime, caloriesPerDay, new HashMap<>(), result);
        return result;
    }

    public static void filterWithRecursion(LinkedList<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay,
                                           Map<LocalDate, Integer> dailyCaloriesMap, List<MealTo> result) {
        if (meals.isEmpty()) return;

        Meal meal = meals.pop();
        dailyCaloriesMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        filterWithRecursion(meals, startTime, endTime, caloriesPerDay, dailyCaloriesMap, result);
        if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
            result.add(createTo(meal, dailyCaloriesMap.get(meal.getDate()) > caloriesPerDay));
        }
    }

    public static List<MealTo> filteredByRecursionWithRunnable(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<MealTo> mealsTo = new ArrayList<>();
        Iterator<Meal> iterator = meals.iterator();

        new Runnable() {
            @Override
            public void run() {
                while (iterator.hasNext()) {
                    Meal meal = iterator.next();
                    caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
                    if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                        run();
                        mealsTo.add(createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
                    }
                }
            }
        }.run();
        return mealsTo;
    }

    public static List<MealTo> filteredByCountDownLatch(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) throws InterruptedException {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<MealTo> mealsTo = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latchCycles = new CountDownLatch(meals.size());
        CountDownLatch latchTasks = new CountDownLatch(meals.size());

        for (Meal meal : meals) {
            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                new Thread(() -> {
                    try {
                        latchCycles.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mealsTo.add(createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
                    latchTasks.countDown();
                }).start();
            } else {
                latchTasks.countDown();
            }
            latchCycles.countDown();
        }
        latchTasks.await();
        return mealsTo;
    }

    public static List<MealTo> filteredByConsumer(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        List<MealTo> result = new ArrayList<>();
        Consumer<Void> consumer = dummy -> {};

        for (Meal meal : meals) {
            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                consumer = consumer.andThen(dummy -> result.add(createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)));
            }
        }
        consumer.accept(null);
        return result;
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
