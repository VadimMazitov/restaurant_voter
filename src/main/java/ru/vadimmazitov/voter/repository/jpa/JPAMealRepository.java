package ru.vadimmazitov.voter.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vadimmazitov.voter.model.Meal;
import ru.vadimmazitov.voter.model.Restaurant;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.repository.JPAUtil;
import ru.vadimmazitov.voter.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JPAMealRepository implements MealRepository {

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
    public Meal save(int adminId, int restaurantId, Meal meal) {
        if (!meal.isNew() && (jpaUtil.getSecuredOnUser(adminId, meal.getId()) == null)) {
            return null;
        }
        meal.setUser(em.getReference(User.class, adminId));
        meal.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int adminId, int restaurantId, int id) {
        if (jpaUtil.getSecuredOnUser(adminId, id) == null)
            return false;
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id) {
        return em.find(Meal.class, id);
    }

    @Override
    public List<Meal> getAllForRestaurant(int restaurantId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId ORDER BY m.price DESC", Meal.class)
                .setParameter("restaurantId", restaurantId).getResultList();
    }
}
