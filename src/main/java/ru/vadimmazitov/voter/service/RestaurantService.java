package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.vadimmazitov.voter.model.Restaurant;
import ru.vadimmazitov.voter.repository.RestaurantRepository;

import java.util.List;

import static ru.vadimmazitov.voter.util.ValidationUtil.checkNotFoundWithId;

@Service("restaurantService")
public class RestaurantService {

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(int adminId, Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(adminId, restaurant);
    }

    public void update(int adminId, Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        repository.save(adminId, restaurant);
    }

    public void delete(int adminId, int id) {
        checkNotFoundWithId(repository.delete(adminId, id), id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

}
