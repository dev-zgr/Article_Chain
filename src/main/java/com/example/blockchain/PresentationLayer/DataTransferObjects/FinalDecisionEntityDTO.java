package com.example.blockchain.PresentationLayer.DataTransferObjects;

import com.example.blockchain.DataLayer.Entities.DecisionStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

/**
 * This class is responsible for holding the final decision of the article.
 * It's a Data Transfer Object for the Final Decision Entity. this means that it's used to transfer data between the
 * HTTP Request and the Service Layer. it's wraps decision_file_hash, decisionPoint and reviewRequestEntityDTO.
 */
@Data
public class FinalDecisionEntityDTO {

    /**
     * This is the hash of the final decision file.
     */
    public String decision_file_hash;

    /**
     * This is the decision point of the final decision.
     */
    @Max(value = 11, message = "Decision point must  be between 0 and 10")
    @Min(value = -1, message = "Decision point must be between 0 and 10")
    public int decisionPoint;

    public DecisionStatus review_type;

    public String review_hash;

    private UUID fileIdentifier;

    /**
     * This constructor is used to create the final decision entity with the decision file hash, decision point and the
     * review request entity.
     *
     * @param decision_file_hash      This is the hash of the final decision file.
     * @param decisionPoint           This is the decision point of the final decision.
     * @param review_type             This is the decision point of the final decision.
     */
    public FinalDecisionEntityDTO(String decision_file_hash, int decisionPoint, DecisionStatus review_type, String review_hash) {
        this.decision_file_hash = decision_file_hash;
        this.decisionPoint = decisionPoint;
        this.review_type = review_type;
        this.review_hash = review_hash;
        this.fileIdentifier = UUID.randomUUID();

    }

    /**
     * This constructor is used to create the final decision entity with the decision file hash, decision point and the
     */
    public FinalDecisionEntityDTO(){
        this.decision_file_hash = "";
        this.decisionPoint = -1;
        this.review_type = null;
        this.review_hash = "";
        this.fileIdentifier = UUID.randomUUID();

    }
}
