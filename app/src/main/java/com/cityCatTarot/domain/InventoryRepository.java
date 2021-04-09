package com.cityCatTarot.domain;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Inventory save(Inventory inventory);

    List<Inventory> findByUserId(Long userId);

    void delete(Long inventoryId);
}
