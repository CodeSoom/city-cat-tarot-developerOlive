package com.cityCatTarot.application;

import com.cityCatTarot.domain.Card;
import com.cityCatTarot.domain.CardRepository;
import com.cityCatTarot.errors.CardNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * 카드에 관한 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional
public class CardService {

    private final Mapper mapper;
    private final CardRepository cardRepository;

    public CardService(Mapper mapper, CardRepository cardRepository) {
        this.mapper = mapper;
        this.cardRepository = cardRepository;
    }

    /**
     * 저장된 모든 카드를 리턴합니다.
     */
    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    /**
     * 전달된 식별자에 해당하는 카드를 리턴합니다.
     *
     * @param cardId 카드 식별자
     * @return 전달된 식별자에 해당하는 카드
     * @throws CardNotFoundException 식별자에 해당하는 카드를 찾을 수 없는 경우
     */
    public Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }
}
