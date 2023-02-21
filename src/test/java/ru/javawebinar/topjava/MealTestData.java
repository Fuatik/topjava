package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;
    public static final LocalDate START_DATE =  LocalDate.of(2020, 1, 12);
    public static final LocalDate END_DATE = LocalDate.of(2020, 1, 12);

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, 1, 12, 10, 0), "Завтрак", 400);

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal (START_SEQ + 5, LocalDateTime.of(2020, 1, 12, 19, 0), "Ужин", 600),
            new Meal (START_SEQ + 4, LocalDateTime.of(2020, 1, 12, 13, 0), "Обед", 1000),
            userMeal
    );
    public static final List<Meal> filteredAdminMeals = Arrays.asList(
            new Meal (START_SEQ + 7,LocalDateTime.of(2020, 1, 12, 12 ,0), "Обед", 1000),
            new Meal (START_SEQ + 6, LocalDateTime.of(2020, 1, 12 , 10, 0), "Завтрак", 500)
    );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, 2, 20, 20, 20), "НОВЫЙ", 666);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDateTime(LocalDateTime.of(2021, 12, 21, 21, 21));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(999);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
