package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewResponseLetterDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is responsible for handling the Review Response Letter HTTP Request.
 * Mainly handles the POST and GET request for the Review Response Letter Entities Stored in Blockchain.
 */
@RestController
public class ReviewResponseLetterController {

    /**
     * The Article Service is used to access the Article Service Layer.
     */
    private final ArticleService articleService;

    /**
     * This constructor is used to inject the Article Service into the Review Response Letter Controller.
     *
     * @param articleService The Article Service is used to access the Article Service Layer.
     */
    @Autowired
    public ReviewResponseLetterController(ArticleService articleService) {
        this.articleService = articleService;
    }


    /**
     * This method is used to handle the POST request for the Review Response Letter.
     *
     * @param reviewResponseLetter The Review Request DTO is used to store the Review Request Letter Data.
     * @return ResponseEntity<String> This returns the status of the POST request.
     * HTTP201 - Created returns when the Review Response Letter is successfully created.
     * HTTP400 - Bad Request returns when the Review Response Letter is not created due to bad data.
     * HTTP500 - Internal Server Error returns when the Review Response Letter is not created due to server error.
     */
    @PostMapping(path = "/review-response", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestBody ReviewResponseLetterDTO reviewResponseLetter) {

        try {
            boolean status = articleService.submitPendingReviewResponse(reviewResponseLetter);
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
