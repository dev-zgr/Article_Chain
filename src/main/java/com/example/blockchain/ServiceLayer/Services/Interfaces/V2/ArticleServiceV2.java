package com.example.blockchain.ServiceLayer.Services.Interfaces.V2;

import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewPendingArticleExtendedDTO;

import java.util.List;

public interface ArticleServiceV2 {
    List<SubmitEntity> getAllVerifiedSubmissions(int pageNo, boolean ascending);

    long getPageCountVerifiedArticle();

    List<ReviewPendingArticleExtendedDTO> getAcceptedReviewByEmailSubmissions(String email, int pageNo, boolean ascending);

    ReviewPendingArticleExtendedDTO getAcceptedReviewByEmailSubmissionsAndTXID(String email, long tx_id);


    long getPageCountAcceptedReviewByEmail(String email);
}
