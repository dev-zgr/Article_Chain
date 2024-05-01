package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * This embeddable is used for defining the structure of the transaction type: submit.
 * It is meant to be embedded within the Submit Entity to store information about an article.
 */
@Data
@Embeddable
@Table(name = "transaction")
public class ArticleEmbeddable {

    private String article_title;
    private String article_type;
    private String article_resField;
    private String article_date;
    private String article_keywords;
    private String paperAbstract;
    private UUID fileIdentifier;

    @Column(length = 4096)
    LinkedList<AuthorEntity> authors;

    /**
     * Constructor for creating an instance of ArticleEmbeddable with all attributes.
     *
     * @param article_title      The title of the article.
     * @param article_type       The type of the article.
     * @param article_resField   The research field of the article.
     * @param article_keywords   Keywords associated with the article.
     * @param author_name        The name of the author(s) of the article.
     * @param author_email       The email address of the author(s).
     * @param institution        The institution to which the author(s) is/are affiliated.
     * @param department         The department within the institution to which the author(s) belongs.
     * @param address            The country, state/city and the zip code of the country the author(s) is/are in.
     */
    public ArticleEmbeddable(String article_title, String article_type, String article_resField,String article_keywords, String author_name, String author_email, String institution, String department, Address address, String title, String paperAbstract,UUID fileIdentifier, LinkedList<AuthorEntity> authors) {
        this.article_title = article_title;
        this.article_type = article_type;
        this.article_resField = article_resField;
        this.article_keywords = article_keywords;
        this.paperAbstract = paperAbstract;
        this.fileIdentifier = fileIdentifier;
        this.authors = authors;
    }

    /**
     * Default constructor for creating an instance of ArticleEmbeddable with default values.
     */
    public ArticleEmbeddable() {
        this.article_title = this.article_type = this.article_resField = this.article_keywords;
        this.fileIdentifier = UUID.randomUUID();
        this.authors = new LinkedList<>();
    }
}
