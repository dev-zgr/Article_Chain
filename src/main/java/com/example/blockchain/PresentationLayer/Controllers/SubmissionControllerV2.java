package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.GenericListResponseDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.V2.ArticleServiceV2;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        response.setPageNumber(articleServiceV2.getPageCount());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
