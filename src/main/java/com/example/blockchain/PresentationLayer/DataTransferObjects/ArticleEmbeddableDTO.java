package com.example.blockchain.PresentationLayer.DataTransferObjects;

import com.example.blockchain.DataLayer.Entities.Address;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
public class ArticleEmbeddableDTO{
    /**
     * Fields for storing the title of the article.
     */
    @NotEmpty(message = "Title is required")
    @Size(max = 100, message = "Title must be up to 100 characters")
    private String article_title;

    /**
     * Fields for storing the type of the article.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "Type must be up to 100 characters")
    private String article_type;


    /**
     * Fields for storing the research field of the article.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String article_resField;

    /**
     * Fields for storing the date when the article was published.
     */
    private String article_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    /**
     * Fields for storing the keywords associated with the article.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String article_keywords;

    @NotEmpty(message = "Type is required")
    @Size(max = 1000, message = "Abstract must be up to 1000 characters")
    private String paperAbstract;

    private UUID fileIdentifier;

    @Valid
    LinkedList<AuthorDTO> authors;


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
    public ArticleEmbeddableDTO(String article_title, String article_type, String article_resField,String article_keywords, String author_name, String author_email, String institution, String department, Address address, String title, String paperAbstract, LinkedList<AuthorDTO> authors) {
        this.article_title = article_title;
        this.article_type = article_type;
        this.article_resField = article_resField;
        this.article_keywords = article_keywords;
        this.paperAbstract = paperAbstract;
        this.authors = authors;
        this.fileIdentifier = UUID.randomUUID();

    }

    /**
     * Default constructor for creating an instance of ArticleEmbeddable with default values.
     */
    public ArticleEmbeddableDTO() {
        this.article_title = this.article_type = this.article_resField = this.article_keywords;
        this.authors = new LinkedList<>();
        this.fileIdentifier = UUID.randomUUID();
    }
}
