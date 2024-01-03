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


    public FinalDecisionEntity(ReviewRequestEntity reviewRequest, String decision_file_hash, int decisionPoint, DecisionStatus review_type){
        super(reviewRequest.getReviewer_name(),reviewRequest.getReviewerResearchField(),reviewRequest.getReviewer_email(),reviewRequest.getReferringTxId());
        this.decision_file_hash = decision_file_hash;
        this.decisionPoint = decisionPoint;
        this.review_type = review_type;
    }

    public FinalDecisionEntity(){
        super();
        this.decisionPoint = 0;
        this.decision_file_hash = null;
        this.review_type = null;
    }





}
