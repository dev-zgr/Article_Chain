package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.SubmissionRequestDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinalDecisionController {

    private final ArticleService articleService;


    @Autowired
    public FinalDecisionController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @PostMapping(path = "/final-decision", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody FinalDecisionEntityDTO finalDecisionEntityDTO) {


        try {

            boolean status = articleService.submitPendingFinalDecision(finalDecisionEntityDTO);

            if (status) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .header("Content-Type", "application/json")
                        .body("Submission created successfully");
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Submission creation due bad data");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Submission creation failed");
        }
    }
}
