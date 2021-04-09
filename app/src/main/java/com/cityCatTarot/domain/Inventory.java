package com.cityCatTarot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Inventory")
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "user_input_subject")
    private String userInputSubject;

    @Column(name = "card_category")
    private String cardCategory;

    @Column(name = "card_image_url")
    private String cardImageUrl;

    @Column(name = "card_title")
    private String cardTitle;

    @Column(name = "card_detail")
    private String cardDetail;
}
