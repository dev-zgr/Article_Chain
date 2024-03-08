package com.example.blockchain.DataLayer.Entities;

import com.example.blockchain.PresentationLayer.DataTransferObjects.AcceptanceEnumDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

/**
 * This entity is used for representing the final decision made on a review request.
 * It extends the ReviewRequestEntity class and includes additional attributes for the decision.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@ToString(callSuper = true)
@DiscriminatorValue("decision")
public class FinalDecisionEntity extends ReviewRequestEntity {

    /**
     * Fields for storing the hash of the decision file.
     */
    @Column(name = "decision_file_hash")
    private String decision_file_hash;

    /**
     * Fields for storing point of the decision.
     */
    @Column(name = "decision_point")
    @Min(value = -1, message = "Decision status must be between 0 and 10")
    @Max(value = 11 , message = "Decision status must be between 0 and 10")
    private int decisionPoint;


    /**
     * Fields for storing the type of the review. (First review, revision etc.)
     */
    @Column(name = "review_type")
    @Enumerated(EnumType.STRING)
    private DecisionStatus review_type;

    @Column(name = "file_identifier_final_decision")
    private UUID fileIdentifier;

    /**
     * Constructor for creating an instance of FinalDecisionEntity with review request details,
     * decision file hash, and decision point status.
     *
     * @param reviewRequest      The review request details inherited from the ReviewRequestEntity class.
     * @param decision_file_hash The hash of the decision file.
     * @param decisionPoint      The status of the final decision point.
     * @param review_type        The type of the review.
     * @param review_hash
     */
    public FinalDecisionEntity(ReviewRequestEntity reviewRequest, String decision_file_hash, int decisionPoint, DecisionStatus review_type, AcceptanceEnumDTO acceptanceEnumDTO,  UUID fileIdentifier){
        super(reviewRequest.getReviewer_name(),reviewRequest.getReviewerResearchField(),reviewRequest.getReviewer_email(),reviewRequest.getManuscriptId(),acceptanceEnumDTO);
        this.decision_file_hash = decision_file_hash;
        this.decisionPoint = decisionPoint;
        this.review_type = review_type;
        this.fileIdentifier = fileIdentifier;
    }

    /**
     * Default constructor for creating an instance of FinalDecisionEntity with default values.
     */
    public FinalDecisionEntity(){
        super();
        this.decisionPoint = 0;
        this.decision_file_hash = null;
        this.review_type = null;
        this.fileIdentifier = UUID.randomUUID();
    }

}
