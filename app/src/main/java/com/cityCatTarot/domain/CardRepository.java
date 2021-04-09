package com.cityCatTarot.domain;

import java.util.List;
import java.util.Optional;

/**
 * 카드 저장소.
 */
public interface CardRepository {
    List<Card> findAll();
    Optional<Card> findById(Long cardId);
}
