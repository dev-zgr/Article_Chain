package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("decision")
public class decisionEntity extends reviewEntity{

    @Column(name = "decision_file_hash")
    private String decision_file_hash;

    @Column(name = "decision_score")
    private int decision_score;

    @Column(name = "decision_status")
    private boolean decision_status;

    public decisionEntity(String decision_file_hash, int decision_score, boolean decision_status){
        this.decision_file_hash = decision_file_hash;
        this.decision_score = decision_score;
        this.decision_status = decision_status;
    }

    public decisionEntity(){
        this.decision_status = false;
        this.decision_score = 0;
        this.decision_file_hash = null;
    }

}
