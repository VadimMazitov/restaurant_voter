package ru.vadimmazitov.voter.repository;

import ru.vadimmazitov.voter.model.Meal;

import java.util.List;

public interface MealRepository {

//    null if restaurant doesn't belong to admin or updated not found
    Meal save(int adminId, int restaurantId, Meal meal);

//    false if restaurant doesn't belong to admin or meal not found
    boolean delete(int adminId, int restaurantId, int id);

//    null if not found
    Meal get(int id);

//    ordered by price desc
    List<Meal> getAllForRestaurant(int restaurantId);

}
