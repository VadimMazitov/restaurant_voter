package ru.vadimmazitov.voter.repository;

import ru.vadimmazitov.voter.HasUser;
import ru.vadimmazitov.voter.model.AbstractBaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JPAUtil {

    @PersistenceContext
    private EntityManager em;

    public AbstractBaseEntity getSecuredOnUser(int userId, int checkedObjectId) {
        HasUser entity = em.find(HasUser.class, checkedObjectId);
        return entity.getUser().getId() == userId ? (AbstractBaseEntity) entity : null;
    }

}
