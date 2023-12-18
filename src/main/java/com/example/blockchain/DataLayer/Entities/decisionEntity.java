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

    @Column(name = "decision_status")
    private int decision_status;

    public decisionEntity(String decision_file_hash, int decision_status){
        this.decision_file_hash = decision_file_hash;
        this.decision_status = decision_status; // 0 means failed, 1 means revise, 2 means passed
    }

    public decisionEntity(){
        this.decision_status = 0;
        this.decision_file_hash = null;
    }

}
