package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is the REST Controller for the ReviewRequestTransactions.
 * It handles the HTTP requests and responses for the Final Decision.
 */
@RestController
public class ReviewRequestController {

    /**
     * Article Service is used for handling the business logic of creating the transactions
     */
    private final ArticleService articleService;

    /**
     * This constructor used to create a ReviewRequestController with the ArticleService
     * @param articleService ArticleService that this controller uses to handle the business logic HTTP requests
     */
    @Autowired
    public ReviewRequestController(ArticleService articleService) {
        this.articleService = articleService;
    }


    /**
     * This method is used for creating a new ReviewRequest transaction for the blockchain. It handles HTTP POST request for the
     * /review-request endpoint. Consumes and produces JSON.
     * @param reviewRequest used for DTO between Rest Controller and HTTP Client
     * @return a ResponseEntity string with string body and HTTP response code.
     * "Review Request created successfully" with HTTP200 returned if transaction created successfully.
     * "Review Request failed creation due bad data" with HTTP400 returned if client request with bad data.
     * "Review Request creation failed due server error" with HTTP500 returned if Internal server error occurs.
     */
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
