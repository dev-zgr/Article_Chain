package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.SubmissionRequestDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is the REST Controller for the Submission Transactions.
 * It handles the HTTP requests and responses for the Submission Transactions.
 */
@RestController
public class SubmissionController {

    /**
     * Article Service is used for handling the business logic of creating the transactions
     */
    private final ArticleService articleService;


    /**
     * This constructor used to create a ReviewRequestController with the ArticleService
     *
     * @param articleService ArticleService that this controller uses to handle the business logic HTTP requests
     */
    @Autowired
    public SubmissionController(ArticleService articleService) {
        this.articleService = articleService;
    }


    /**
     * This method is used for creating a new Submission transaction for the blockchain. It handles HTTP POST request for the
     * /submission endpoint. Consumes and produces JSON.
     *
     * @param submissionRequestDTO used for DTO between Rest Controller and HTTP Client
     * @return a ResponseEntity string with string body and HTTP response code.
     * "Submission created successfully" with HTTP200 returned if transaction created successfully.
     * "Submission creation failed due bad data" with HTTP400 returned if client request with bad data.
     * "Submission creation failed" with HTTP500 returned if Internal server error occurs.
     */
    @PostMapping(path = "/submission", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody SubmissionRequestDTO submissionRequestDTO) {


        try {

            boolean status = articleService.submitPendingSubmission(
                    submissionRequestDTO.articleEmbeddable,
                    submissionRequestDTO.paperHash);

            if (status) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .header("Content-Type", "application/json")
                        .body("Submission created successfully");
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Submission creation failed due bad data");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Submission creation failed");
        }
    }


    @GetMapping(path = "/pending-submission", produces = "application/json")
    public ResponseEntity<List<SubmitEntity>> getPendingSubmissions(
            @RequestParam(name = "category", defaultValue = "null", required = false) String category,
            @RequestParam(name = "title", defaultValue = "null", required = false) String title,
            @RequestParam(name = "author", defaultValue = "null", required = false) String author,
            @RequestParam(name = "department", defaultValue = "null", required = false) String department,
            @RequestParam(name = "institution", defaultValue = "null", required = false) String institution,
            @RequestParam(name = "keyword", defaultValue = "null", required = false) String keyword
    ) {

        try {
            List<SubmitEntity> desiredSubmissions = articleService.getReviewPendingArticles(
                    handleNullParameter(category),
                    handleNullParameter(title),
                    handleNullParameter(author),
                    handleNullParameter(department),
                    handleNullParameter(institution),
                    handleNullParameter(keyword)
            );
            if (desiredSubmissions.isEmpty()) {
                return ResponseEntity
                        .status(204)
                        .body(null);
            }
            return ResponseEntity
                    .status(200)
                    .body(desiredSubmissions);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }


    }


    @GetMapping(path = "/verified-submission", produces = "application/json")
    public ResponseEntity<List<SubmitEntity>> getVerifiedSubmissions(
            @RequestParam(name = "category", defaultValue = "null", required = false) String category,
            @RequestParam(name = "title", defaultValue = "null", required = false) String title,
            @RequestParam(name = "author", defaultValue = "null", required = false) String author,
            @RequestParam(name = "department", defaultValue = "null", required = false) String department,
            @RequestParam(name = "institution", defaultValue = "null", required = false) String institution,
            @RequestParam(name = "keyword", defaultValue = "null", required = false) String keyword) {



        try {
            List<SubmitEntity> desiredSubmissions = articleService.getVerifiedSubmissions(
                    handleNullParameter(category),
                    handleNullParameter(title),
                    handleNullParameter(author),
                    handleNullParameter(department),
                    handleNullParameter(institution),
                    handleNullParameter(keyword)
                    );
            if (desiredSubmissions.isEmpty()) {
                return ResponseEntity
                        .status(204)
                        .body(null);
            }
            return ResponseEntity
                    .status(200)
                    .body(desiredSubmissions);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }


    }


    private String handleNullParameter(String parameter) {
        if ("null".equals(parameter)) {
            return null;
        }
        return parameter;
    }


}
