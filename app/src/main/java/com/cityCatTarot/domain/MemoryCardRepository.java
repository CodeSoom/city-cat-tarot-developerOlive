package com.cityCatTarot.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryCardRepository implements CardRepository {

    private final EntityManager entityManager;

    public MemoryCardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Card> findAll() {
        return entityManager.createQuery("select c from Card c", Card.class)
                .getResultList();
    }

    @Override
    public Optional<Card> findById(Long cardId) {
        Card card = entityManager.find(Card.class, cardId);
        return Optional.ofNullable(card);
    }
}
