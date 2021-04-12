package com.cityCatTarot.application;

import com.cityCatTarot.domain.Inventory;
import com.cityCatTarot.domain.InventoryRepository;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.dto.InventorySaveData;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final Mapper mapper;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public InventoryService(Mapper mapper, InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.mapper = mapper;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    public Inventory saveCardDetail(InventorySaveData inventorySaveData,
                                    Long userId) throws AccessDeniedException {

        return inventoryRepository.save(
                mapper.map(inventorySaveData, Inventory.class));

    }

    public List<Inventory> findCardListWithUserId(Long userId) {
        return inventoryRepository.findByUserId(userId);
    }

//    public List<Inventory> findCardListWithUserIdAndCardId(Long userId, Long cardId) {
//        List<Inventory> cardListForDelete = inventoryRepository.findByUserIdAndCardId(userId, cardId);
//
//        return cardListForDelete;
//    }

    public void deleteCardDetail(Long inventoryId){
        inventoryRepository.delete(inventoryId);
    }
}
