package com.example.blockchain.PresentationLayer.DataTransferObjects;

import jakarta.validation.constraints.Email;

/**
 * This class is responsible for holding the Review Request for the  articles.
 * It's a Data Transfer Object for the ReviewRequest Entity. this means that it's used to transfer data between the
 * HTTP Request and the Service Layer. It wraps reviewers name , reviewers research field , review e-mail and referring submission id.
 */
public class ReviewRequestDTO {
    /**
     * This is the name of the reviewer.
     */
    public String reviewerName;
    /**
     * This is the research field of the reviewer.
     */
    public String reviewerResearchField;
    /**
     * This is the email of the reviewer.
     */
    @Email
    public String reviewerEmail;

    /**
     * This is the id of the submission that the review request is referring to.
     */
    public long referringTxId;

    public AcceptanceEnumDTO acceptanceStatus;

    /**
     * This constructor is used to create the review request entity with the reviewer name, reviewer research field,
     * @param reviewerName name of the reviewer
     * @param reviewerResearchField research field of the reviewer
     * @param reviewerEmail email of the reviewer must be a valid email
     * @param referringTxId id of the submission that the review request is referring to
     */
    public ReviewRequestDTO(String reviewerName, String reviewerResearchField, String reviewerEmail, long referringTxId,AcceptanceEnumDTO acceptanceStatus) {
        this.reviewerName = reviewerName;
        this.reviewerResearchField = reviewerResearchField;
        this.reviewerEmail = reviewerEmail;
        this.referringTxId = referringTxId;
        this.acceptanceStatus = acceptanceStatus;
    }

    /**
     * This constructor is used to create the review request entity with the reviewer name, reviewer research field,
     */
    public ReviewRequestDTO() {
        this.reviewerName = this.reviewerResearchField = this.reviewerEmail = "";
        referringTxId = -1;
        acceptanceStatus =null;
    }
}
