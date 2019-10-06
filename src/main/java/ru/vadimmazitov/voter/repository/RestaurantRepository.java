package ru.vadimmazitov.voter.repository;

import ru.vadimmazitov.voter.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

//    null if updated doesn't belong to admin or not exist
    Restaurant save(int adminId, Restaurant restaurant);

//    false if restaurant doesn't belong to admin
    boolean delete(int adminId, int id);

//    null if not found
    Restaurant get(int id);

    //    null if not found
    Restaurant getReference(int id);

//    ordered by name desc
    List<Restaurant> getAll();

}
