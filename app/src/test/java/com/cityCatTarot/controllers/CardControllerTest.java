package com.cityCatTarot.controllers;

import com.cityCatTarot.application.AuthenticationService;
import com.cityCatTarot.application.CardService;
import com.cityCatTarot.domain.Card;
import com.cityCatTarot.errors.CardNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<Card> cards = new ArrayList<>();

    private final Long EXISTING_CARD_ID_1 = 1L;
    private final Long EXISTING_CARD_ID_2 = 2L;
    private final Long NOT_EXISTING_ID = 999L;
    private final String EXISTING_CARD_CATEGORY = "todayTarot";
    private final String IMAGE_URL = "url";
    private final String CARD_TITLE_1 = "마법사";
    private final String CARD_TITLE_2 = "여사제";
    private final String CARD_DETAIL_1 = "마법사 카드 입니다.";
    private final String CARD_DETAIL_2 = "여사제 카드 입니다.";
    private final int TOTAl_CARD_COUNT = 2;

    @AfterEach
    void clear() {
        Mockito.reset(cardService);
    }

    @BeforeEach
    void setUp() {
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

        given(cardService.getCards()).willReturn(cards);

        given(cardService.getCard(EXISTING_CARD_ID_1)).willReturn(card_fool);

        given(cardService.getCard(NOT_EXISTING_ID))
                .willThrow(new CardNotFoundException(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("GET /tarotChat 요청은 카드 목록에 저장된 데이터가 있으면 200 코드와 저장되어있는 카드 목록을 응답한다.")
    void getListsWithExistedCards() throws Exception {
        mockMvc.perform(
                get("/tarotChat")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(CARD_TITLE_1)))
                .andExpect(content().string(containsString(CARD_TITLE_2)))
                .andExpect(jsonPath("$", hasSize(TOTAl_CARD_COUNT)));
    }

    @Test
    @DisplayName("GET /tarotChat 요청은 카드 목록에 저장된 데이터가 없으면 200코드와 저장되어있는 카드 목록을 응답한다.")
    void getListWithNoCard() throws Exception {
        Mockito.reset(cardService);

        mockMvc.perform(get("/tarotChat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("GET /tarotChat/{cardCategory}/{cardId} 요청은 저장된 cardId가 주어지면 200 코드와 cardId에 일치하는 카드를 응답한다.")
    void detailWithExistedCardId() throws Exception {
        mockMvc.perform(get("/tarotChat/{cardCategory}/{cardId}",
                EXISTING_CARD_CATEGORY, EXISTING_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("cardId").value(EXISTING_CARD_ID_1))
                .andExpect(jsonPath("cardCategory").value(EXISTING_CARD_CATEGORY))
                .andExpect(jsonPath("cardImageUrl").value(IMAGE_URL))
                .andExpect(jsonPath("cardTitle").value(CARD_TITLE_1))
                .andExpect(jsonPath("cardDetail").value(CARD_DETAIL_1));
    }

    @Test
    @DisplayName("GET /tarotChat/{cardCategory}/{cardId} 요청은 저장 되어있지 않은 cardId가 주어지면 404 코드를 응답한다.")
    void detailWithNotExistedCardId() throws Exception {
        mockMvc.perform(get("/tarotChat/{cardCategory}/{cardId}",
                EXISTING_CARD_CATEGORY, NOT_EXISTING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
