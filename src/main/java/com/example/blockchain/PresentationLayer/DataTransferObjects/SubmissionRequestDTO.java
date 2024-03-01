package com.example.blockchain.PresentationLayer.DataTransferObjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


/**
 * This class is responsible for holding Submission requests.
 * It's a Data Transfer Object for the Submission request. this means that it's used to transfer data between the
 * HTTP Request and the Service Layer. it's wraps articleEmbeddable and paper hash.
 */
@Validated
@Data
public class SubmissionRequestDTO {
    /**
     * This is the article embeddable of the submission request.
     */
    @NotNull
    @Valid
    public ArticleEmbeddableDTO articleEmbeddable;

    /**
     * This is the hash of the paper of the submission request.
     */
    @NotNull
    public String paperHash;

    /**
     * This constructor is used to create the submission request entity with the article embeddable and the paper hash.
     * @param articleEmbeddable article embeddable of the submission request
     * @param paperHash hash of the paper of the submission request
     */
    public SubmissionRequestDTO(ArticleEmbeddableDTO articleEmbeddable, String paperHash) {
        this.articleEmbeddable = articleEmbeddable;
        this.paperHash = paperHash;
    }

    /**
     * This constructor is used to create the submission request entity with the article embeddable and the paper hash.
     */
    public SubmissionRequestDTO() {
        this.articleEmbeddable = new ArticleEmbeddableDTO();
        this.paperHash = "";
    }
}
