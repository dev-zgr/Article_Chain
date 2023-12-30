package com.example.blockchain.DataLayer.Entities;

import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("decision")
public class FinalDecisionEntity extends ReviewRequestEntity {

    @Column(name = "decision_file_hash")
    private String decision_file_hash;

    @Column(name = "decision_status")
    @Min(value = 0, message = "Decision status must be between 0 and 10")
    @Max(value = 10 , message = "Decision status must be between 0 and 10")
    private int decisionPoint;

    public FinalDecisionEntity(ReviewRequestEntity reviewRequest, String decision_file_hash, int decisionPoint){
        super(reviewRequest.getReviewer_name(),reviewRequest.getReviewerResearchField(),reviewRequest.getReviewer_email(),reviewRequest.getReferringSubmissionId());
        this.decision_file_hash = decision_file_hash;
        this.decisionPoint = decisionPoint; // 0 means failed, 1 means revise, 2 means passed
    }

    public FinalDecisionEntity(){
        super();
        this.decisionPoint = 0;
        this.decision_file_hash = null;
    }

}
