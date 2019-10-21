package ru.vadimmazitov.voter.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.vadimmazitov.voter.HasUser;
import ru.vadimmazitov.voter.model.AbstractBaseEntity;
import ru.vadimmazitov.voter.model.Meal;
import ru.vadimmazitov.voter.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JPAUtil {

    @PersistenceContext
    private EntityManager em;


//    TODO refactor and try to remove @Transactional
    @Transactional(readOnly = true)
    public AbstractBaseEntity getSecuredOnUser(int userId, int checkedObjectId, Class<? extends HasUser> cls) {
        if (cls == Restaurant.class) {
            Restaurant entity = em.find(Restaurant.class, checkedObjectId);
            return entity.getUser().getId() == userId ? entity : null;
        } else if (cls == Meal.class) {
            Meal entity = em.find(Meal.class, checkedObjectId);
            return entity.getUser().getId() == userId ? entity : null;
        }
        return null;

    }

}
