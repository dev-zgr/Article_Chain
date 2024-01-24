package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * This embeddable is used for defining the structure of the transaction type: submit.
 * It is meant to be embedded within the Submit Entity to store information about an article.
 */
@Data
@Embeddable
@Table(name = "transaction")
public class ArticleEmbeddable {


    /**
     * Fields for storing the title of the article.
     */
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be up to 100 characters")
    private String article_title;

    /**
     * Fields for storing the type of the article.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type must be up to 100 characters")
    private String article_type;

    /**
     * Fields for storing the name of the author(s) of the article.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String author_name;

    /**
     * Fields for storing the research field of the article.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String article_resField;

    /**
     * Fields for storing the date when the article was published.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String article_date;

    /**
     * Fields for storing the keywords associated with the article.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String article_keywords;


    /**
     * Fields for storing the email address of the author(s). It's must be a valid email address.
     */
    @Email
    private String author_email;

    /**
     * Fields for storing the institution to which the author(s) is/are affiliated.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "institution must be up to 100 characters")
    private String institution;

    /**
     * Fields for storing the department within the institution to which the author(s) belongs.
     */
    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "department must be up to 100 characters")
    private String department;

    private Address address;

    /**
     * Constructor for creating an instance of ArticleEmbeddable with all attributes.
     *
     * @param article_title      The title of the article.
     * @param article_type       The type of the article.
     * @param article_resField   The research field of the article.
     * @param article_date       The date when the article was published.
     * @param article_keywords   Keywords associated with the article.
     * @param author_name        The name of the author(s) of the article.
     * @param author_email       The email address of the author(s).
     * @param institution        The institution to which the author(s) is/are affiliated.
     * @param department         The department within the institution to which the author(s) belongs.
     * @param address            The country, state/city and the zip code of the country the author(s) is/are in.
     */
    public ArticleEmbeddable(String article_title, String article_type, String article_resField, String article_date, String article_keywords, String author_name, String author_email, String institution, String department, Address address) {
        this.article_title = article_title;
        this.article_type = article_type;
        this.article_resField = article_resField;
        this.article_date = article_date;
        this.article_keywords = article_keywords;
        this.author_name = author_name;
        this.author_email = author_email;
        this.institution = institution;
        this.department = department;
        this.address = address;
    }

    /**
     * Default constructor for creating an instance of ArticleEmbeddable with default values.
     */
    public ArticleEmbeddable() {
        this.article_title = this.article_type = this.article_resField = this.article_date = this.article_keywords = this.author_name = this.author_email = this.institution = this.department = null;
    }
}
