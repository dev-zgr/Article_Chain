package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.FileEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewPendingArticleExtendedDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.SubmissionRequestDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * This class is the REST Controller for the Submission Transactions.
 * It handles the HTTP requests and responses for the Submission Transactions.
 */
@RestController
@CrossOrigin(origins = "*")
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
    @PostMapping(path = "/submission" , produces = "application/json")
    public ResponseEntity<String> createSubmission(@RequestPart("file") MultipartFile file, @RequestPart("jsonBody") @Valid SubmissionRequestDTO submissionRequestDTO,
                                                   BindingResult bindingResult) {

        try {

//            if (bindingResult.hasErrors()) {
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body("Validation failed. Please check your request data.");
//            }
            boolean status = articleService.submitPendingSubmission(
                    submissionRequestDTO.articleEmbeddable,file,
                    submissionRequestDTO.paperHash);

            if (status) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .header("Content-Type", "application/json")
                        .body("Submission created successfully");
            }else{
                throw new Exception("Submission creation failed");
            }
        } catch (Exception e) {
            System.out.println(e);
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
            @RequestParam(name = "keyword", defaultValue = "null", required = false) String keyword,
            @RequestParam(name = "tx_id", defaultValue = "null", required = false) String tx_id)
            {

        try {
            List<SubmitEntity> desiredSubmissions = articleService.getReviewPendingArticles(
                    handleNullParameter(category),
                    handleNullParameter(title),
                    handleNullParameter(author),
                    handleNullParameter(department),
                    handleNullParameter(institution),
                    handleNullParameter(keyword),
                    parseString(tx_id)
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
            @RequestParam(name = "keyword", defaultValue = "null", required = false) String keyword,
            @RequestParam(name = "tx_id", defaultValue = "null", required = false) String tx_id,
            @RequestParam(name = "article_type", defaultValue = "null", required = false) String article_type
    ) {


        try {
            List<SubmitEntity> desiredSubmissions = articleService.getVerifiedSubmissions(
                    handleNullParameter(category),
                    handleNullParameter(title),
                    handleNullParameter(author),
                            handleNullParameter(department),
                            handleNullParameter(institution),
                            handleNullParameter(keyword),
                            parseString(tx_id),
                            handleNullParameter(article_type)
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

    @GetMapping(path = "/get-accepted-review-by-email-submission", produces = "application/json")
    public ResponseEntity<List<ReviewPendingArticleExtendedDTO>> getAcceptedReviewByEmailSubmissions(
            @RequestParam(name = "email", defaultValue = "test@test.com", required = true) String email
    ) {
        try {
            List<ReviewPendingArticleExtendedDTO> desiredSubmissions = articleService.getAcceptedReviewByEmailSubmissions(
                    handleNullParameter(email)
            );
            if (desiredSubmissions.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(List.of());
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(desiredSubmissions);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping(path = "/transaction/{id}", produces = "application/json")
    public ResponseEntity<TransactionEntity> getTransactionById(@PathVariable Long id){
        try {
            TransactionEntity transaction = articleService.getPendingTransactionById(id);
            if(transaction == null){
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(null);
            }else {
                return ResponseEntity.ok()
                        .body(transaction);
            }
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping(path = "/rejected-submission", produces = "application/json")
    public ResponseEntity<List<SubmitEntity>> findRejectedSubmissions(
            @RequestParam(name = "category", defaultValue = "null", required = false) String category,
            @RequestParam(name = "title", defaultValue = "null", required = false) String title,
            @RequestParam(name = "author", defaultValue = "null", required = false) String author,
            @RequestParam(name = "department", defaultValue = "null", required = false) String department,
            @RequestParam(name = "institution", defaultValue = "null", required = false) String institution,
            @RequestParam(name = "keyword", defaultValue = "null", required = false) String keyword,
            @RequestParam(name = "tx_id", defaultValue = "null", required = false) String tx_id)
    {
        try {
            List<SubmitEntity> desiredSubmissions = articleService.getRejectedSubmissions(
                    handleNullParameter(category),
                    handleNullParameter(title),
                    handleNullParameter(author),
                    handleNullParameter(department),
                    handleNullParameter(institution),
                    handleNullParameter(keyword),
                    parseString(tx_id)
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

    @GetMapping(path = "/file")
    public ResponseEntity<byte[]> getFileByUUID(@RequestParam(name = "uuid") UUID uuid){
        try {
            byte[] file = articleService.getFileByUUID(uuid);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "filename.pdf");
            if(file == null){
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(null);
            }else {
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(file);
            }
        } catch (Exception e){
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

    private Long parseString(String number){
        if ("null".equals(number)) {
            return null;
        }
        return Long.parseLong(number);
    }
}
