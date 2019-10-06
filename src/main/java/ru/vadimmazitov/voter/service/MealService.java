package ru.vadimmazitov.voter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.vadimmazitov.voter.model.Meal;
import ru.vadimmazitov.voter.repository.MealRepository;

import java.util.List;

import static ru.vadimmazitov.voter.util.ValidationUtil.checkNotFoundWithId;

@Service("mealService")
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int adminId, int restaurantId, Meal meal) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(adminId, restaurantId, meal);
    }

    public void update(int adminId, int restaurantId, Meal meal) {
        Assert.notNull(meal, "meal must not be null");
        repository.save(adminId, restaurantId, meal);
    }

    public void delete(int adminId, int restaurantId, int id) {
        checkNotFoundWithId(repository.delete(adminId, restaurantId, id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Meal> getAllForRestaurant(int restaurantId) {
        return repository.getAllForRestaurant(restaurantId);
    }



}
