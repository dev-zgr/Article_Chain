package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.Data;

/**
 * This entity is used for representing a submission in the blockchain.
 * It extends the TransactionEntity class and includes attributes specific to a submission.
 */
@Entity
@Data
@DiscriminatorValue("submit")
public class SubmitEntity extends TransactionEntity {

    ArticleEmbeddable article;

    @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long submisionId;

    @Column(name = "paper_hash")
    private String paper_hash;

    /**
     * Constructor for creating an instance of SubmitEntity with paper hash and article details.
     *
     * @param paperHash The hash of the submitted paper.
     * @param article   The details of the article being submitted.
     */
    public SubmitEntity(String paperHash, ArticleEmbeddable article) {
        this.paper_hash = paperHash;
        this.article = article;
    }

    /**
     * Default constructor for creating an instance of SubmitEntity with default values.
     */
    public SubmitEntity() {
        super();
        this.article = new ArticleEmbeddable();
        this.paper_hash = "";
    }

}
