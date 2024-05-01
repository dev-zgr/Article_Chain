package com.example.blockchain.PresentationLayer.DataTransferObjects;

/**
 * This class is responsible for holding the Review Response Letter of the article.
 * It's a Data Transfer Object for the Review Response Letter. this means that it's used to transfer data between the
 * HTTP Request and the Service Layer. It wraps review response Letter hash and referring submission id.
 */
public class ReviewResponseLetterDTO {
    /**
     * This is the hash of the review response letter.
     */
    public String reviewResponseLetterHash;

    /**
     * This is the id of the submission that the review response letter is referring to.
     */
    public long referringSubmissionId;

    /**
     * This constructor start the review response entity with the review response letter hash and the referring submission id
     * @param reviewResponseLetterHash hash of the review response letter to secure integrity of the letter
     * @param referringSubmissionId id of the submission that the review response letter is referring to
     */
    public ReviewResponseLetterDTO(String reviewResponseLetterHash, long referringSubmissionId) {
        this.reviewResponseLetterHash = reviewResponseLetterHash;
        this.referringSubmissionId = referringSubmissionId;
    }

    /**
     * This constructor start the review response entity with the review response letter hash and the referring submission id
     */
    public ReviewResponseLetterDTO() {
        this.reviewResponseLetterHash = "";
        this.referringSubmissionId = -1;
    }
}
