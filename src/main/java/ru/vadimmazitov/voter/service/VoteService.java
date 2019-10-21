package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * This method creates a vote. Restaurant is set manually, because there is no write-access to it from Jackson.
     * This is done for security reasons so that it is not possible to change restaurant of the vote when updating
     */
//    TODO refactor
    public Vote create(int userId, int restaurantId, Vote vote) {
        Assert.notNull(vote, "vote must not be null");
        Restaurant restaurant = restaurantRepository.getReference(restaurantId);
        vote.setRestaurant(restaurant);
        Vote created = repository.save(userId, vote);

        List<Vote> votes = repository.getAllForRestaurant(restaurantId);
        int rating = votes.stream().mapToInt(x -> x.getVote()).sum();
        restaurant.setRating(rating);
        restaurantRepository.updateRating(restaurant);

        return created;
    }

//    TODO refactor
    public void update(int userId, Vote updated) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        Vote actual = repository.get(userId, updated.getId());
        LocalDateTime actualDateTime = actual.getDateTime();
        LocalDate actualDate = actualDateTime.toLocalDate();

        LocalTime elevenPM = LocalTime.of(23, 0);

        if (!actualDate.equals(nowDate) && !nowTime.isBefore(elevenPM))
            throw new IllegalArgumentException("votes can be updated until 11pm on the voting day");

        int restaurantId = updated.getRestaurant().getId();
        Restaurant restaurant = restaurantRepository.getReference(restaurantId);

        List<Vote> votes = repository.getAllForRestaurant(restaurantId);
        int rating = votes.stream().mapToInt(x -> x.getVote()).sum();
        restaurant.setRating(rating);
        restaurantRepository.updateRating(restaurant);

        repository.save(userId, updated);
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
