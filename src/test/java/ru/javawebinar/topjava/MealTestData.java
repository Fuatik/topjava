package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int USER_MEAL = START_SEQ + 3;
    public static final int ADMIN_MEAL = START_SEQ + 6;
    public static final int GUEST_MEAL = START_SEQ + 9;
    public static final int NOT_FOUND = 10;
    public static final LocalDate START_DATE =  LocalDate.parse("2020-01-12");
    public static final LocalDate END_DATE = LocalDate.parse("2020-01-12");

    public static final Meal userMeal = new Meal(USER_MEAL, LocalDateTime.parse("2020-01-12T10:00"), "Завтрак", 400);

    public static final Meal guest = new Meal(GUEST_MEAL, LocalDateTime.parse("2020-01-12T10:00"), "Завтрак", 600);

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal (100005, LocalDateTime.parse("2020-01-12T19:00"), "Ужин", 600),
            new Meal (100004, LocalDateTime.parse("2020-01-12T13:00"), "Обед", 1000),
            new Meal (100003, LocalDateTime.parse("2020-01-12T10:00"), "Завтрак", 400)
    );
    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal (100007,LocalDateTime.parse("2020-01-12T12:00"), "Обед", 1000),
            new Meal (100006, LocalDateTime.parse("2020-01-12T10:00"), "Завтрак", 500)
    );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2020-02-20T20:20"), "НОВЫЙ", 666);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDateTime(LocalDateTime.parse("2021-12-21T21:21"));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(999);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
