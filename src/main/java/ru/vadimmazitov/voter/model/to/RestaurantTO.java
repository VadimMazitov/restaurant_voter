package ru.vadimmazitov.voter.model.to;

import java.util.Collections;
import java.util.List;

public class RestaurantTO extends BaseTO {

    private Integer rating;

    private List<MealTO> meals;

    public RestaurantTO() {
    }

    public RestaurantTO(Integer id, Integer rating, List<MealTO> meals) {
        super(id);
        this.rating = rating;
        this.meals = meals;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<MealTO> getMeals() {
        return meals == null ? Collections.emptyList() : meals;
    }

    public void setMeals(List<MealTO> meals) {
        this.meals = meals.isEmpty() ? Collections.emptyList() : meals;
    }

    @Override
    public String toString() {
        return "RestaurantTO{" +
                "id=" + id +
                "rating=" + rating +
                ", meals=" + meals +
                '}';
    }
}
