package ru.vadimmazitov.voter.repository.jpa;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vadimmazitov.voter.model.User;
import ru.vadimmazitov.voter.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional(readOnly = true)
public class JPAUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
            return user;
//            TODO check if id is returned
        } else {
            return em.merge(user);
        }
    }

    @Override
    public boolean delete(int id) {
        Query query = em.createQuery("DELETE FROM User u WHERE u.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    @Transactional
    public User get(int id) {
        return em.find(User.class, id);
    }
}
