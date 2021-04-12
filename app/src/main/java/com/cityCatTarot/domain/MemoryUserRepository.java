package com.cityCatTarot.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryUserRepository implements UserRepository {

    private final EntityManager entityManager;

    public MemoryUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {

        return Optional.of(entityManager.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    @Override
    public Optional<User> findByEmailForRegister(String email) {

        try {
            Query q = entityManager.createQuery("select u from User u where u.email = :email", User.class);
            q.setParameter("email", email);
            return (Optional<User>) q.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailForLogin(String email) {
        return Optional.of(entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    public boolean existsByEmail(String email) {
        if (findByEmailForRegister(email).isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        Query query = entityManager.createQuery("delete from User AS u where u.id = :userId")
                .setParameter("userId", id);
        query.executeUpdate();
    }
}
