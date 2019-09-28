DELETE FROM meals;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
    ('User', 'user@yandex.ru', 'userPassword'),
    ('Admin_Palermo', 'admin@yandex.ru', 'admin_password');

INSERT INTO user_roles (user_id, role) VALUES
    (100000, 'ROLE_USER'),
    (100001, 'ROLE_ADMIN'),
    (100001, 'ROLE_USER');

INSERT INTO restaurants (name, rating, user_id) VALUES
    ('Palermo', 73, 100001);

INSERT INTO meals (restaurant_id, name, price) VALUES
    (100002, 'Breakfast', 70),
    (100002, 'Lunch', 50),
    (100002, 'Dinner', 100),
    (100002, 'Supper', 120);

INSERT INTO votes (user_id, restaurant_id, vote) VALUES
    (100000, 100006, 74);