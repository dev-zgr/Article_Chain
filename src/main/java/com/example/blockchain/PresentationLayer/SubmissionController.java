package com.example.blockchain.PresentationLayer;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.ServiceLayer.Services.Interfaces.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }


    @PostMapping(path = "/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody  ArticleEmbeddable article) {

        try {
            submissionService.submitArticle(article);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body("Submission created successfully");


        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Submission creation failed");
        }

    }


}
