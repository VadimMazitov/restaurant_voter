package ru.vadimmazitov.voter.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.vadimmazitov.voter.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "restaurants")
//TODO Cache
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "rating", nullable = false, columnDefinition = "REAL DEFAULT 5.0")
    private Double rating = 5.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotNull(groups = View.Persist.class)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Meal> menu;

    public Restaurant() {}

    public Restaurant(int id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName());
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public void setMenu(List<Meal> menu) {
        this.menu = menu;
    }


    @Override
    public String toString() {
        return "Restaurant{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", user=" + user +
                ", menu=" + menu +
                '}';
    }
}
