package com.cityCatTarot.controllers;


import com.cityCatTarot.application.InventoryService;
import com.cityCatTarot.domain.Inventory;
import com.cityCatTarot.dto.InventoryResultData;
import com.cityCatTarot.dto.InventorySaveData;
import com.cityCatTarot.security.UserAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/inventory", produces = "application/json; charset=UTF8")
public class InventoryController {

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping(value="/post-card", produces = "application/json; charset=UTF8")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    InventoryResultData create(
            @RequestBody InventorySaveData inventorySaveData,
            UserAuthentication authentication
    ) throws AccessDeniedException {
        Long authenticatedUserId = authentication.getUserId();

        Inventory inventory = inventoryService.saveCardDetail(inventorySaveData, authenticatedUserId);
        return getInventoryData(inventory);
    }

    public InventoryResultData getInventoryData(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        return InventoryResultData.builder()
                .inventoryId(inventory.getInventoryId())
                .cardId(inventory.getCardId())
                .cardCategory(inventory.getCardCategory())
                .userInputSubject(inventory.getUserInputSubject())
                .cardImageUrl(inventory.getCardImageUrl())
                .cardTitle(inventory.getCardTitle())
                .cardDetail(inventory.getCardDetail())
                .build();
    }

    @GetMapping(value="/get-card/{userId}", produces = "application/json; charset=UTF8")
    @PreAuthorize("isAuthenticated()")
    public List<Inventory> getCardList(
            @PathVariable Long userId,
            UserAuthentication authentication)
            throws AccessDeniedException {

        Long authenticatedUserId = authentication.getUserId();
        return inventoryService.findCardListWithUserId(authenticatedUserId);
    }

    @DeleteMapping(value="delete-card/{inventoryId}", produces = "application/json; charset=UTF8")
    @PreAuthorize("isAuthenticated()")
    void deleteCard(@PathVariable Long inventoryId){
        inventoryService.deleteCardDetail(inventoryId);
    }
}
