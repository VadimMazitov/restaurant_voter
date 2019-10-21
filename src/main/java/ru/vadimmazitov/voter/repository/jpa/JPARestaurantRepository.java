package ru.vadimmazitov.voter.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vadimmazitov.voter.model.Restaurant;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.repository.JPAUtil;
import ru.vadimmazitov.voter.repository.RestaurantRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JPARestaurantRepository implements RestaurantRepository {

    @PersistenceContext
    private EntityManager em;

    private JPAUtil jpaUtil;

    @Autowired(required = false)
    public void setJpaUtil(JPAUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    @Transactional
    @SuppressWarnings("Duplicates")
    public Restaurant save(int adminId, Restaurant restaurant) {
        if (!restaurant.isNew() && (jpaUtil.getSecuredOnUser(adminId, restaurant.getId(), Restaurant.class) == null))
            return null;
        restaurant.setUser(em.getReference(User.class, adminId));
        if (restaurant.isNew()) {
            em.persist(restaurant);
            return restaurant;
        } else {
            return em.merge(restaurant);
        }
    }

    @Override
    @Transactional
    public void updateRating(Restaurant restaurant) {
        em.merge(restaurant);
    }

    @Override
    @Transactional
    public boolean delete(int adminId, int id) {
        if (jpaUtil.getSecuredOnUser(adminId, id, Restaurant.class) == null)
            return false;
        Query query = em.createQuery("DELETE FROM Restaurant r WHERE r.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public Restaurant get(int id) {
        return em.createQuery("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menu WHERE r.id=:id", Restaurant.class)
                            .setParameter("id", id)
                            .getSingleResult();
    }

    @Override
    public Restaurant getReference(int id) {
        return em.getReference(Restaurant.class, id);
    }

    @Override
    public List<Restaurant> getAll() {
        return em.createQuery("SELECT r FROM Restaurant r ORDER BY r.name DESC", Restaurant.class).getResultList();
    }
}
