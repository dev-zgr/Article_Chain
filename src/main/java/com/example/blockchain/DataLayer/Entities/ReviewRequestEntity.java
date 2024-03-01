package com.example.blockchain.DataLayer.Entities;

import com.example.blockchain.PresentationLayer.DataTransferObjects.AcceptanceEnumDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    /**
     * Fields for storing the name of the reviewer.
     */
    @Column(name = "reviewer_name")
    private String reviewer_name;

    /**
     * Fields for storing the research field of the reviewer.
     */
    @Column(name = "reviewer_research_field")
    private String reviewerResearchField;

    /**
     * Fields for storing the email of the reviewer.It's must be a valid email.
     */
    @Email
    @Column(name = "reviewer_email")
    private String reviewer_email;

    /**
     * Fields for storing the ID of the referring transaction.
     */
    @Column(name = "manuscript_id")
    private long manuscriptId;

    @Column(name = "Acceptance")
    @Enumerated(EnumType.STRING)
    private AcceptanceEnumDTO acceptanceEnumDTO;

    /**
     * Constructor for creating an instance of ReviewRequestEntity with reviewer details and referring transaction ID.
     *
     * @param reviewer_name          The name of the reviewer.
     * @param reviewer_resField      The research field of the reviewer.
     * @param reviewer_email         The email address of the reviewer.
     * @param manuscriptId          The ID of the referring transaction.
     */
    public ReviewRequestEntity(String reviewer_name, String reviewer_resField, String reviewer_email, long manuscriptId , AcceptanceEnumDTO acceptanceEnumDTO) {
        super();
        this.reviewer_name = reviewer_name;
        this.reviewerResearchField = reviewer_resField;
        this.reviewer_email = reviewer_email;
        this.manuscriptId = manuscriptId;
        this.acceptanceEnumDTO = acceptanceEnumDTO;
    }
    /**
     * Default constructor for creating an instance of ReviewRequestEntity with default values.
     */
    public ReviewRequestEntity(){
        this.reviewer_name = this.reviewerResearchField = this.reviewer_email = "";
        manuscriptId = -1;
    }

}
