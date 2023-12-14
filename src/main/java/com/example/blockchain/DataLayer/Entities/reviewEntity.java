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

    @Column(name = "reviewer_category")
    private String reviewer_category;

    @Column(name = "reviewer_institution")
    private String reviewer_institution;

    @Column(name = "reviewer_email")
    private String reviewer_email;

    @Column(name = "reviewer_phone")
    private String reviewer_phone;

    public reviewEntity(String reviewer_name, String reviewer_category, String reviewer_institution, String reviewer_email, String reviewer_phone) {
        this.reviewer_name = reviewer_name;
        this.reviewer_category = reviewer_category;
        this.reviewer_institution = reviewer_institution;
        this.reviewer_email = reviewer_email;
        this.reviewer_phone = reviewer_phone;
    }

    public reviewEntity(){
        this.reviewer_name = this.reviewer_category = this.reviewer_institution = this.reviewer_email = this.reviewer_phone = null;
    }

}
