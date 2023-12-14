package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("final")
public class finalEntity extends TransactionEntity{

    @Column(name = "final_file_hash")
    private String final_file_hash;

    @Column(name = "final_score")
    private int final_score;

    @Column(name = "final_status")
    private boolean final_status;

    public finalEntity(String final_file_hash, int final_score, boolean final_status){
        this.final_file_hash = final_file_hash;
        this.final_score = final_score;
        this.final_status = final_status;
    }

    public finalEntity(){
        this.final_status = false;
        this.final_score = 0;
        this.final_file_hash = null;
    }

}
