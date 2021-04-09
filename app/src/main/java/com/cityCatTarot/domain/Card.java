package com.cityCatTarot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 카드 정보.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Card")
public class Card {

    @Id
    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "card_category")
    private String cardCategory;

    @Column(name = "card_image_url")
    private String cardImageUrl;

    @Column(name = "card_title")
    private String cardTitle;

    @Column(name = "card_detail")
    private String cardDetail;
}
