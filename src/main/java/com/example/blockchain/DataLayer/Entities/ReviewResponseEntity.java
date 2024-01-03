package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class is used to store the review response entity
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue("review-response")
public class ReviewResponseEntity extends TransactionEntity {

    /**
     * This column is used to store the review response letter hash
     */
    @Column(name = "review_response_letter_hash")
    private String reviewResponseLetterHash;

    /**
     * This column is used to store the referring submission id
     */
    @Column(name = "referring_submission_id")
    private long referringSubmissionId;

    /**
     * This constructor start the review response entity with the review response letter hash and the referring submission id
     * @param reviewResponseLetterHash
     * @param referringSubmissionId
     */
    public ReviewResponseEntity(String reviewResponseLetterHash, long referringSubmissionId) {
        super();
        this.reviewResponseLetterHash = reviewResponseLetterHash;
        this.referringSubmissionId = referringSubmissionId;
    }

    /**
     * This constructor start the review response entity with the review response letter hash and the referring submission id
     */
    public ReviewResponseEntity() {
        super();
        this.reviewResponseLetterHash = "";
        this.referringSubmissionId = -1;
    }


}
