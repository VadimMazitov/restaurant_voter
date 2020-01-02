package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.vadimmazitov.voter.model.Restaurant;
import ru.vadimmazitov.voter.model.Vote;
import ru.vadimmazitov.voter.repository.RestaurantRepository;
import ru.vadimmazitov.voter.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service("voteService")
public class VoteService {

    private final VoteRepository repository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public VoteService(VoteRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    /*
     * This method creates a vote. Restaurant is set manually, because there is no write-access to it from Jackson.
     * This is done for security reasons so that it is not possible to change restaurant of the vote when updating
     */
    @Transactional
    public Vote create(int userId, int restaurantId, Vote vote) {
        Assert.notNull(vote, "vote must not be null");

        LocalDateTime now = LocalDateTime.now();
        Restaurant restaurant = restaurantRepository.getReference(restaurantId);

        vote.setDateTime(now);
        vote.setRestaurant(restaurant);
        Vote created = repository.save(userId, vote);

        updateRating(restaurant);

        return created;
    }

    @Transactional
    public void update(int userId, Vote updated) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        Vote actual = repository.get(userId, updated.getId());
        LocalDateTime actualDateTime = actual.getDateTime();
        LocalTime actualTime = actual.getDateTime().toLocalTime();
        LocalDate actualDate = actualDateTime.toLocalDate();

        LocalTime elevenPM = LocalTime.of(23, 0);
        LocalTime twelvePM = LocalTime.MIDNIGHT;

        if (actualTime.isBefore(elevenPM)) {
            if (!actualDate.equals(nowDate) && !nowTime.isBefore(elevenPM))
                throw new IllegalArgumentException("The vote can be updated until 11pm on " + nowDate.toString());
        } else {
            if (!(actualDate.equals(nowDate) && nowTime.isBefore(twelvePM)) ||
                    (actualDate.equals(nowDate.plusDays(1)) && nowTime.isBefore(elevenPM))) {
                throw new IllegalArgumentException("The vote can be updated until 11pm on " + nowDate.plusDays(1).toString());
            }
        }

        actual.setVote(updated.getVote());
        repository.save(userId, actual);

        int restaurantId = actual.getRestaurant().getId();

        updateRating(restaurantId);
    }

    private void updateRating(int restaurantId) {
        Restaurant restaurant = restaurantRepository.getReference(restaurantId);
        updateRating(restaurant);
    }

    private void updateRating(Restaurant restaurant) {
        List<Vote> votes = repository.getAllForRestaurant(restaurant.getId());
        int size = votes.size();
        int rating = votes.stream().mapToInt(Vote::getVote).sum() / size;
        restaurant.setRating(rating);
        restaurantRepository.updateRating(restaurant);
    }

    public List<Vote> getAllForRestaurant(int restaurantId) {
        return repository.getAllForRestaurant(restaurantId);
    }

    public List<Vote> getAllForUser(int userId) {
        return repository.getAllForUser(userId);
    }

    public Vote get(int userId, int id) {
        return repository.get(userId, id);
    }
}
