package com.example.blockchain.DataLayer.Entities;

import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Column(name = "decision_file_hash")
    private String decision_file_hash;

    @Column(name = "decision_status")
    @Min(value = 0, message = "Decision status must be between 0 and 10")
    @Max(value = 10 , message = "Decision status must be between 0 and 10")
    private int decisionPoint;

    @Column(name = "review_hash")
    private String review_hash;

    @Column(name = "review_type")
    private DecisionStatus review_type;

    /**
     * Constructor for creating an instance of FinalDecisionEntity with review request details,
     * decision file hash, and decision point status.
     *
     * @param reviewRequest      The review request details inherited from the ReviewRequestEntity class.
     * @param decision_file_hash The hash of the decision file.
     * @param decisionPoint      The status of the final decision point.
     * @param review_type        The type of the review.
     */
    public FinalDecisionEntity(ReviewRequestEntity reviewRequest, String decision_file_hash, int decisionPoint, DecisionStatus review_type){
        super(reviewRequest.getReviewer_name(),reviewRequest.getReviewerResearchField(),reviewRequest.getReviewer_email(),reviewRequest.getReferringTxId());
        this.decision_file_hash = decision_file_hash;
        this.decisionPoint = decisionPoint;
        this.review_type = review_type;
    }

    /**
     * Default constructor for creating an instance of FinalDecisionEntity with default values.
     */
    public FinalDecisionEntity(){
        super();
        this.decisionPoint = 0;
        this.decision_file_hash = null;
        this.review_type = null;
    }

}
