package com.cityCatTarot.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemoryRoleRepository implements RoleRepository {

    private final EntityManager entityManager;

    public MemoryRoleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findAllByUserId(Long id) {
        return entityManager.createQuery("select r from Role r where r.id = :id",
                Role.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Role save(Role role) {
        entityManager.persist(role);
        return role;
    }
}
