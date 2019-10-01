package ru.vadimmazitov.voter.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.model.Vote;
import ru.vadimmazitov.voter.repository.VoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JPAVoteRepository implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Vote save(int userId, Vote vote) {
        vote.setUser(em.getReference(User.class, userId));
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            return em.merge(vote);
        }
    }

    @Override
    public List<Vote> getAllForRestaurant(int restaurantId) {
        return em.createQuery("SELECT v FROM Vote v WHERE v.restaurant.id=:restaurantId ORDER BY v.dateTime DESC ", Vote.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}
