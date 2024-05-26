package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.GenericListResponseDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewPendingArticleExtendedDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.V2.ArticleServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v2")
public class SubmissionControllerV2 {
    private final ArticleServiceV2 articleServiceV2;

    @Autowired
    public SubmissionControllerV2(ArticleServiceV2 articleServiceV2) {
        this.articleServiceV2 = articleServiceV2;
    }


    @GetMapping(path = "/verified-articles", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericListResponseDTO<SubmitEntity>> getAllCourses(
            @RequestParam(value = "page-no", defaultValue = "0") int pageNo,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {
        GenericListResponseDTO<SubmitEntity> response  = new GenericListResponseDTO<>();
        response.setData(articleServiceV2.getAllVerifiedSubmissions(pageNo, ascending));
        response.setPageNumber(articleServiceV2.getPageCountVerifiedArticle());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping(path = "/get-accepted-review-by-email-submission", produces = "application/json")
    public ResponseEntity<GenericListResponseDTO<ReviewPendingArticleExtendedDTO>> getAcceptedReviewByEmailSubmissions(
            @RequestParam(name = "email", defaultValue = "test@test.com", required = true) String email,
            @RequestParam(value = "page-no", defaultValue = "0") int pageNo,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {
        try {
            List<ReviewPendingArticleExtendedDTO> desiredSubmissions = articleServiceV2.getAcceptedReviewByEmailSubmissions(
                    handleNullParameter(email), pageNo, ascending
            );
            GenericListResponseDTO<ReviewPendingArticleExtendedDTO> response = new GenericListResponseDTO<>();
            response.setData(desiredSubmissions);
            response.setPageNumber(articleServiceV2.getPageCountAcceptedReviewByEmail(email));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping(path = "/get-accepted-review-by-email-submission-and-txid", produces = "application/json")
    public ResponseEntity<ReviewPendingArticleExtendedDTO> getAcceptedReviewByEmailSubmissionsAndTXID(
            @RequestParam(name = "tx-id", required = true) long txId, @RequestParam(name = "email", defaultValue = "test@test.com", required = true) String email) {
        try {
            ReviewPendingArticleExtendedDTO desiredSubmissions = articleServiceV2.getAcceptedReviewByEmailSubmissionsAndTXID(
                    email, txId
            );
            if(desiredSubmissions == null){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null);

            }else{
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(desiredSubmissions);
            }
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
