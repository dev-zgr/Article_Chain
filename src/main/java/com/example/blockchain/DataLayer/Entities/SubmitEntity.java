package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.Data;

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

    public SubmitEntity(String paperHash, ArticleEmbeddable article) {
        this.paper_hash = paperHash;
        this.article = article;
    }

    public SubmitEntity() {
        super();
        this.article = new ArticleEmbeddable();
        this.paper_hash = "";
    }

}
