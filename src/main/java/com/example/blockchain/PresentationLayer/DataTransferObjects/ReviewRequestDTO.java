package com.example.blockchain.PresentationLayer.DataTransferObjects;

public class ReviewRequestDTO {
    public String reviewerName;
    public String reviewerResearchField;
    public String reviewerEmail;
    public long referringSubmissionId;


    public ReviewRequestDTO(String reviewerName, String reviewerResearchField, String reviewerEmail, long referringSubmissionId) {
        this.reviewerName = reviewerName;
        this.reviewerResearchField = reviewerResearchField;
        this.reviewerEmail = reviewerEmail;
        this.referringSubmissionId = referringSubmissionId;
    }

    public ReviewRequestDTO() {
        this.reviewerName = this.reviewerResearchField = this.reviewerEmail = "";
        referringSubmissionId = -1;
    }
}
