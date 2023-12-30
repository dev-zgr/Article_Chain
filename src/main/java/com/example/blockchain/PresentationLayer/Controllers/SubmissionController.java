package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.SubmissionRequestDTO;
import com.example.blockchain.ServiceLayer.Models.TransactionHolder;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubmissionController {

    private final ArticleService articleService;


    @Autowired
    public SubmissionController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @PostMapping(path = "/submission", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody SubmissionRequestDTO submissionRequestDTO) {


        try {

            boolean status = articleService.submitPendingSubmission(
                    submissionRequestDTO.getArticleEmbeddable(),
                    submissionRequestDTO.getPaperHash());

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


    @GetMapping(path = "/submission", produces = "application/json")
    public ResponseEntity<List<SubmitEntity>> getAllSubmissions(
            @RequestParam(defaultValue = "null") String department,
            @RequestParam(defaultValue = "null") String authorName,
            @RequestParam(defaultValue = "null") String articleKeywords,
            @RequestParam(defaultValue = "null") String institution,
            @RequestParam(defaultValue = "null") String article_type,
            @RequestParam(defaultValue = "null") String article_title,
            @RequestParam(defaultValue = "false") String verified
    ) {


        return null;
    }

    //TODO get mapping ekle


}
