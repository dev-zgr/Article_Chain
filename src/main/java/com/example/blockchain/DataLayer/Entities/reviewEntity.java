package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("review")
public class reviewEntity extends TransactionEntity{

    @Column(name = "reviewer_name")
    private String reviewer_name;

    @Column(name = "reviewer_resField")
    private String reviewer_resField;

    @Column(name = "reviewer_email")
    private String reviewer_email;

    public reviewEntity(String reviewer_name, String reviewer_resField, String reviewer_email) {
        this.reviewer_name = reviewer_name;
        this.reviewer_resField = reviewer_resField;
        this.reviewer_email = reviewer_email;
    }

    public reviewEntity(){
        this.reviewer_name = this.reviewer_resField = this.reviewer_email = null;
    }

}
