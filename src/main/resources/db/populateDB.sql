DELETE FROM meals;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2020-01-12T10:00', 'Завтрак', '400', 100000),
       ('2020-01-12T13:00', 'Обед', '1000', 100000),
       ('2020-01-12T19:00', 'Ужин', '600', 100000),
       ('2020-01-12T10:00', 'Завтрак', '500', 100001),
       ('2020-01-12T12:00', 'Обед', '1000', 100001),
       ('2020-01-13T18:00', 'Ужин', '700', 100001);