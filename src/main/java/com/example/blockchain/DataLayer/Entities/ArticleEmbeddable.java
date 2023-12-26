package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
@Table(name = "transaction")
public class ArticleEmbeddable {


    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be up to 100 characters")
    private String article_title;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type must be up to 100 characters")
    private String article_type;
    private String article_resField;
    private String article_date;
    private String article_keywords;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String author_name;

    @Email
    private String author_email;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "institution must be up to 100 characters")
    private String institution;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "department must be up to 100 characters")
    private String department;

    public ArticleEmbeddable(String article_title, String article_type, String article_resField, String article_date, String article_keywords, String author_name, String author_email, String institution, String department) {
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

    public ArticleEmbeddable() {
        this.article_title = this.article_type = this.article_resField = this.article_date = this.article_keywords = this.author_name = this.author_email = this.institution = this.department = null;
    }
}
