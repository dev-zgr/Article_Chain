package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This entity is used for representing a review request in the blockchain.
 * It extends the TransactionEntity class and includes attributes specific to a review request.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue("review")
public class ReviewRequestEntity extends TransactionEntity{

    @Column(name = "reviewer_name")
    private String reviewer_name;

    @Column(name = "reviewer_research_field")
    private String reviewerResearchField;

    @Column(name = "reviewer_email")
    private String reviewer_email;

    @Column(name = "referring_tx_id")
    private long referringTxId;

    /**
     * Constructor for creating an instance of ReviewRequestEntity with reviewer details and referring transaction ID.
     *
     * @param reviewer_name          The name of the reviewer.
     * @param reviewer_resField      The research field of the reviewer.
     * @param reviewer_email         The email address of the reviewer.
     * @param referringTxId          The ID of the referring transaction.
     */
    public ReviewRequestEntity(String reviewer_name, String reviewer_resField, String reviewer_email, long referringTxId) {
        super();
        this.reviewer_name = reviewer_name;
        this.reviewerResearchField = reviewer_resField;
        this.reviewer_email = reviewer_email;
        this.referringTxId = referringTxId;
    }
    /**
     * Default constructor for creating an instance of ReviewRequestEntity with default values.
     */
    public ReviewRequestEntity(){
        this.reviewer_name = this.reviewerResearchField = this.reviewer_email = "";
        referringTxId = -1;
    }

}
