package ru.vadimmazitov.voter.repository;

import ru.vadimmazitov.voter.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository {

//    null if updated vote doesn't belong to user or time for update is up, or user has already voted for the restaurant
    Vote save(int userId, Vote vote);

//    null if not found or doesn't belong to user
    Vote get(int userId, int id);

//    ordered by datetime desc
    List<Vote> getAllForRestaurant(int restaurantId);

//    ordered by datetime desc
    List<Vote> getAllForUser(int userId);




}
