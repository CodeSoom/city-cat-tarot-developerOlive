package com.cityCatTarot.errors;

/**
 * 카드를 찾을 수 없는 예외.
 */
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long cardId) {
        super("Card not found: " + cardId);
    }
}
