package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("submit")
public class submitEntity extends TransactionEntity {

    @Column(name = "article_name")
    private String article_name;

    @Column(name = "article_type")
    private String article_type;

    @Column(name = "article_category")
    private String article_category;

    @Column(name = "author_name")
    private String author_name;

    @Column(name = "author_email")
    private String author_email;

    @Column(name = "author_phone")
    private String author_phone;

    @Column(name = "institution")
    private String institution;

    @Column(name = "article_date")
    private String article_date;

    @Column(name = "department");
    private String department;

    public submitEntity(String article_name, String article_type, String article_category, String author_name, String author_email, String author_phone, String institution, String date){
        this.article_name = article_name;
        this.article_type = article_type;
        this.article_category = article_category;
        this.author_name = author_name;
        this.author_email = author_email;
        this.author_phone = author_phone;
        this.institution = institution;
        this.article_date = date;
    }

    public submitEntity() {
        this.article_name = this.article_type = this.article_category = this.author_name = this.author_email = this.author_phone = this.institution = this.article_date = null;
    }

}
