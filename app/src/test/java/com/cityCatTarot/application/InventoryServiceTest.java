package com.cityCatTarot.application;

import com.cityCatTarot.domain.Inventory;
import com.cityCatTarot.domain.InventoryRepository;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.dto.InventorySaveData;
import com.cityCatTarot.errors.CardNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InventoryServiceTest {

    private InventoryService inventoryService;

    private InventoryRepository inventoryRepository = mock(InventoryRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private final List<Inventory> inventories1 = new ArrayList<>();
    private final List<Inventory> inventories2 = new ArrayList<>();

    private Long userId_1 = 7L;
    private Long cardId_1 = 7L;
    private String userInputSubject_1 = "사용자입력제목1";
    private String cardCategory_1 = "카테고리1";
    private String cardImageUrl_1 = "url1";
    private String cardTitle_1 = "카드제목1";
    private String cardDetail_1 = "카드내용1";
    private Long inventoryId_2 = 10L;
    private Long userId_2 = 10L;
    private Long cardId_2 = 10L;
    private String userInputSubject_2 = "사용자입력제목2";
    private String cardCategory_2 = "카테고리2";
    private String cardImageUrl_2 = "url2";
    private String cardTitle_2 = "카드제목2";
    private String cardDetail_2 = "카드내용2";

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        inventoryService = new InventoryService(mapper, inventoryRepository, userRepository);

        given(inventoryRepository.save(any(Inventory.class))).will(invocation -> {
            Inventory source = invocation.getArgument(0);
            return Inventory.builder()
                    .userId(source.getUserId())
                    .cardId(source.getCardId())
                    .userInputSubject(source.getUserInputSubject())
                    .cardCategory(source.getCardCategory())
                    .cardImageUrl(source.getCardImageUrl())
                    .cardTitle(source.getCardTitle())
                    .cardDetail(source.getCardDetail())
                    .build();
        });

        Inventory inventorySaveData1 = Inventory.builder()
                .userId(userId_1)
                .cardId(cardId_1)
                .userInputSubject(userInputSubject_1)
                .cardCategory(cardCategory_1)
                .cardImageUrl(cardImageUrl_1)
                .cardTitle(cardTitle_1)
                .cardDetail(cardDetail_1)
                .build();
        inventories1.add(inventorySaveData1);
        given(inventoryRepository.findByUserId(userId_1)).willReturn(inventories1);

        Inventory inventorySaveData2 = Inventory.builder()
                .inventoryId(inventoryId_2)
                .userId(userId_2)
                .cardId(cardId_2)
                .userInputSubject(userInputSubject_2)
                .cardCategory(cardCategory_2)
                .cardImageUrl(cardImageUrl_2)
                .cardTitle(cardTitle_2)
                .cardDetail(cardDetail_2)
                .build();
        inventories2.add(inventorySaveData2);
        given(inventoryRepository.findByUserId(userId_2)).willReturn(inventories2);

    }

    @Test
    @DisplayName("saveCardDetail은 유효한 정보가 주어지면 보관함에 카드를 저장한다.")
    void saveCardDetail() {
        InventorySaveData inventorySaveData = InventorySaveData.builder()
                .userId(userId_1)
                .cardId(cardId_1)
                .userInputSubject(userInputSubject_1)
                .cardCategory(cardCategory_1)
                .cardImageUrl(cardImageUrl_1)
                .cardTitle(cardTitle_1)
                .cardDetail(cardDetail_1)
                .build();

        Inventory inventory = inventoryService.saveCardDetail(inventorySaveData, userId_1);

        assertThat(inventory.getUserId()).isEqualTo(userId_1);
        assertThat(inventory.getCardId()).isEqualTo(cardId_1);
        assertThat(inventory.getUserInputSubject()).isEqualTo(userInputSubject_1);
        assertThat(inventory.getCardCategory()).isEqualTo(cardCategory_1);
        assertThat(inventory.getCardImageUrl()).isEqualTo(cardImageUrl_1);
        assertThat(inventory.getCardTitle()).isEqualTo(cardTitle_1);
        assertThat(inventory.getCardDetail()).isEqualTo(cardDetail_1);

        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("findCardListWithUserId은 식별자에 해당하는 회원이 저장한 카드 목록을 불러온다.")
    void findCardListWithUserId() {
        List<Inventory> inventories = inventoryService.findCardListWithUserId(userId_2);

        assertThat(inventories.size()).isEqualTo(1);
        assertThat(inventories.get(0).getUserId()).isEqualTo(userId_2);
        assertThat(inventories.get(0).getCardId()).isEqualTo(cardId_2);
        assertThat(inventories.get(0).getUserInputSubject()).isEqualTo(userInputSubject_2);
        assertThat(inventories.get(0).getCardCategory()).isEqualTo(cardCategory_2);
        assertThat(inventories.get(0).getCardImageUrl()).isEqualTo(cardImageUrl_2);
        assertThat(inventories.get(0).getCardTitle()).isEqualTo(cardTitle_2);
        assertThat(inventories.get(0).getCardDetail()).isEqualTo(cardDetail_2);
    }

    @Test
    @DisplayName("deleteCardDetail은 올바른 식별자가 주어지면 그 식별자에 해당하는 카드를 삭제한다.")
    void deleteCardDetail(){
        inventoryService.deleteCardDetail(userId_2);

        verify(inventoryRepository).delete(userId_2);
    }
}
