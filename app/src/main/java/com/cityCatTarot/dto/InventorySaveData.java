package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventorySaveData {

    @Mapping("inventoryId")
    private Long inventoryId;

    @Mapping("userId")
    private Long userId;

    @Mapping("cardId")
    private Long cardId;

    @Mapping("userInputSubject")
    private String userInputSubject;

    @Mapping("cardCategory")
    private String cardCategory;

    @Mapping("cardImageUrl")
    private String cardImageUrl;

    @Mapping("cardTitle")
    private String cardTitle;

    @Mapping("cardDetail")
    private String cardDetail;
}
