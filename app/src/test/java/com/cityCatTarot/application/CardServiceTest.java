package com.cityCatTarot.application;

import com.cityCatTarot.domain.Card;
import com.cityCatTarot.domain.CardRepository;
import com.cityCatTarot.errors.CardNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CardServiceTest {

    private CardService cardService;

    private final CardRepository cardRepository =
            mock(CardRepository.class);

    private final List<Card> cards = new ArrayList<>();

    private final Long EXISTING_CARD_ID_1 = 1L;
    private final Long EXISTING_CARD_ID_2 = 2L;
    private final Long NOT_EXISTING_ID = 999L;
    private final String EXISTING_CARD_CATEGORY = "todayTarot";
    private final String NOT_EXISTING_CARD_CATEGORY = "notCategory";
    private final String IMAGE_URL = "url";
    private final String CARD_TITLE_1 = "마법사";
    private final String CARD_TITLE_2 = "여사제";
    private final String CARD_DETAIL_1 = "마법사 카드 입니다.";
    private final String CARD_DETAIL_2 = "여사제 카드 입니다.";
    private final int TOTAl_CARD_COUNT = 2;

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        cardService = new CardService(mapper, cardRepository);

        Card card_fool = Card.builder()
                .cardId(EXISTING_CARD_ID_1)
                .cardCategory(EXISTING_CARD_CATEGORY)
                .cardImageUrl(IMAGE_URL)
                .cardTitle(CARD_TITLE_1)
                .cardDetail(CARD_DETAIL_1)
                .build();
        cards.add(card_fool);

        Card card_magician = Card.builder()
                .cardId(EXISTING_CARD_ID_2)
                .cardCategory(EXISTING_CARD_CATEGORY)
                .cardImageUrl(IMAGE_URL)
                .cardTitle(CARD_TITLE_2)
                .cardDetail(CARD_DETAIL_2)
                .build();
        cards.add(card_magician);


        given(cardRepository.findAll()).willReturn(cards);

        given(cardRepository.findById(EXISTING_CARD_ID_2)).willReturn(Optional.of(card_magician));
    }

        @Test
        void getCards() {
            List<Card> cards = cardService.getCards();

            Card card = cards.get(0);
            assertThat(card.getCardTitle()).isEqualTo(CARD_TITLE_1);
            assertThat(cards.size()).isEqualTo(TOTAl_CARD_COUNT);
        }

        @Test
        void getCardWithExistedCardId() {
            Card card = cardService.getCard(EXISTING_CARD_ID_2);

            assertThat(card).isNotNull();
            assertThat(card.getCardTitle()).isEqualTo(CARD_TITLE_2);
        }

        @Test
        void getCardWithNoExistedCardId() {
            assertThatThrownBy(() -> cardService.getCard(NOT_EXISTING_ID))
                    .isInstanceOf(CardNotFoundException.class);
        }
}
