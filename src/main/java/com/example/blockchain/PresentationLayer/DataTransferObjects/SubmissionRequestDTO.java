package com.example.blockchain.PresentationLayer.DataTransferObjects;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;


/**
 * This class is responsible for holding Submission requests.
 * It's a Data Transfer Object for the Submission request. this means that it's used to transfer data between the
 * HTTP Request and the Service Layer. it's wraps articleEmbeddable and paper hash.
 */
public class SubmissionRequestDTO {
    /**
     * This is the article embeddable of the submission request.
     */
    public ArticleEmbeddable articleEmbeddable;

    /**
     * This is the hash of the paper of the submission request.
     */
    public String paperHash;

    /**
     * This constructor is used to create the submission request entity with the article embeddable and the paper hash.
     * @param articleEmbeddable article embeddable of the submission request
     * @param paperHash hash of the paper of the submission request
     */
    public SubmissionRequestDTO(ArticleEmbeddable articleEmbeddable, String paperHash) {
        this.articleEmbeddable = articleEmbeddable;
        this.paperHash = paperHash;
    }

    /**
     * This constructor is used to create the submission request entity with the article embeddable and the paper hash.
     */
    public SubmissionRequestDTO() {
        this.articleEmbeddable = new ArticleEmbeddable();
        this.paperHash = "";
    }
}
