package ru.vadimmazitov.voter.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.vadimmazitov.voter.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "votes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"}, name = "votes_unique_user_restaurant_idx")})
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    @Column(name = "vote")
    @NotNull
    private Integer vote;

    public Vote() {}

    public Vote(int id, int vote) {
        super(id);
        this.vote = vote;
    }

    public Vote(Vote vote) {
        this(vote.getId(), vote.getVote());
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Vote{" +
                ", id=" + id +
                "restaurant=" + restaurant +
                ", user=" + user +
                ", vote=" + vote +
                '}';
    }
}
