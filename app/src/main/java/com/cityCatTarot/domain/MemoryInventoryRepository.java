package com.cityCatTarot.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryInventoryRepository implements InventoryRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public MemoryInventoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Inventory save(Inventory inventory) {
        entityManager.persist(inventory);
        return inventory;
    }

    @Override
    public List<Inventory> findByUserId(Long userId) {
        return entityManager.createQuery(
                "select i from Inventory i where i.userId = :userId", Inventory.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void delete(Long inventoryId) {
        Query query = entityManager.createQuery("delete from Inventory AS i where i.inventoryId = :inventoryId")
                .setParameter("inventoryId", inventoryId);
        query.executeUpdate();
    }

}
