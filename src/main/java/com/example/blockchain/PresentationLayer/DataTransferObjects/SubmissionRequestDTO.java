package com.example.blockchain.PresentationLayer.DataTransferObjects;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import lombok.Data;

@Data
public class SubmissionRequestDTO {
    private ArticleEmbeddable articleEmbeddable;
    private String paperHash;

    public SubmissionRequestDTO(ArticleEmbeddable articleEmbeddable, String paperHash) {
        this.articleEmbeddable = articleEmbeddable;
        this.paperHash = paperHash;
    }

    public SubmissionRequestDTO() {
        this.articleEmbeddable = new ArticleEmbeddable();
        this.paperHash = "";
    }
}
