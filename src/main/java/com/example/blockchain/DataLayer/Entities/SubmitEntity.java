package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("submit")
public class SubmitEntity extends TransactionEntity {

    ArticleEmbeddable article;

    public SubmitEntity(String paperHash, String abstractHash, ArticleEmbeddable article) {
        super(paperHash, abstractHash);
        this.article = article;
    }

    public SubmitEntity() {
        super();
        this.article = new ArticleEmbeddable();
    }

}
