package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("submit")
public class submitEntity extends TransactionEntity {

    @Column(name = "article_title")
    private String article_title;

    @Column(name = "article_type")
    private String article_type;

    @Column(name = "article_resField")
    private String article_resField;

    @Column(name = "article_date")
    private String article_date;

    @Column(name = "article_keywords")
    private String article_keywords;

    @Column(name = "author_name")
    private String author_name;

    @Column(name = "author_email")
    private String author_email;

    @Column(name = "institution")
    private String institution;

    @Column(name = "department")
    private String department;

    public submitEntity(String article_title, String article_type, String article_resField, String article_date, String article_keywords, String author_name, String author_email, String institution, String department){
        this.article_title = article_title;
        this.article_type = article_type;
        this.article_resField = article_resField;
        this.article_date = article_date;
        this.article_keywords = article_keywords;
        this.author_name = author_name;
        this.author_email = author_email;
        this.institution = institution;
        this.department = department;
    }

    public submitEntity() {
        this.article_title = this.article_type = this.article_resField = this.article_date = this.article_keywords = this.author_name = this.author_email = this.institution = this.department = null;
    }

}
