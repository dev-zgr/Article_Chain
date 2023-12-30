package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.SubmissionRequestDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ReviewRequestController {

    private final ArticleService articleService;

    @Autowired
    public ReviewRequestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(path = "/review-request", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody ReviewRequestDTO reviewRequest) {


        try {

            boolean status = articleService.submitPendingReviewRequest(reviewRequest);

            if (status) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .header("Content-Type", "application/json")
                        .body("Review Request created successfully");
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Review Request failed creation due bad data");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Review Request creation failed due server error");
        }
    }

}
