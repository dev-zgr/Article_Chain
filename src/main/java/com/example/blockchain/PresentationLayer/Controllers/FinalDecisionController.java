package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.ServiceLayer.Exceptions.NoSuchReviewRequest;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This class is the REST Controller for the Final Decision Transactions.
 * it handles the HTTP requests and responses for the Final Decision.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FinalDecisionController {


    /**
     * Article Service is used for handling the business logic of creating the transactions
     */
    private final ArticleService articleService;


    /**
     * This constructor used to create a FinalDecisionController with the ArticleService
     * @param articleService ArticleService that this controller uses to handle the business logic HTTP requests
     */
    @Autowired
    public FinalDecisionController(ArticleService articleService) {
        this.articleService = articleService;
    }


    /**
     * This method is used for creating a new FinalDecisionTransaction for the blockchain. It handles HTTP POST request for the
     * /final-decision endpoint. Consumes and produces JSON.
     * @param finalDecisionEntityDTO used for DTO between Rest Controller and HTTP Client
     * @return a ResponseEntity string with string body and HTTP response code.
     * "Submission created successfully" with HTTP200 returned if transaction created successfully.
     * "Submission creation due bad data" with HTTP400 returned if client request with bad data.
     * "Submission creation failed" with HTTP500 returned if Internal server error occurs.
     */
    @PostMapping(path = "/final-decision", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestParam(name = "tx_id") long txId ,
                                                   @RequestPart("file") MultipartFile file,
                                                   @RequestPart("jsonBody") @Valid FinalDecisionEntityDTO finalDecisionEntityDTO) {

        try {
            boolean status = articleService.submitFinalDecision(finalDecisionEntityDTO, file, txId);
            if(status){
                return ResponseEntity
                        .status(200)
                        .body("Final Decision successfully created");
            }else {
                throw new Exception("couldn't added");
            }
        }catch (NoSuchReviewRequest e){
            return ResponseEntity
                    .status(400)
                    .body("There is no such review Request!");
        }catch (IOException e) {
            return ResponseEntity
                    .status(400)
                    .body("Bad Data!");
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Final Decision creation failed");
        }
    }


}
