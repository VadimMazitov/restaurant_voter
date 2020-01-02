package ru.vadimmazitov.voter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.vadimmazitov.voter.HasUser;
import ru.vadimmazitov.voter.View;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"}, name = "votes_unique_user_restaurant_idx")})
public class Vote extends AbstractBaseEntity implements HasUser {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    @JsonIgnore
    private User user;

    @Column(name = "vote", nullable = false)
    @NotNull
    private Integer vote;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    public Vote() {}

    public Vote(int id, int vote, Restaurant restaurant) {
        super(id);
        this.vote = vote;
        this.restaurant = restaurant;
    }

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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
                "id=" + id +
                ", restaurant=" + restaurant +
                ", user=" + user +
                ", vote=" + vote +
                ", datetime=" + dateTime +
                '}';
    }
}
