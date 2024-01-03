package com.example.blockchain.ServiceLayer.Services.Interfaces;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import com.example.blockchain.DataLayer.Entities.ReviewResponseEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewResponseLetterDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

/**
 * This interface is used to define the methods that will be used in the SubmissionServiceImplementation class.
 * Primary purpose of this interface is to define abstract set of methods that will be used
 * to perform businesses logic operations on the Submissions that will be in the transaction.
 */
public interface ArticleService {

    /**
     * This method is used to create a new Submission Transaction in latest block.
     * @return void This returns nothing.
     */
    void submitArticle(ArticleEmbeddable articleEmbeddable);
    public boolean submitPendingSubmission(ArticleEmbeddable articleEmbeddable, String paperHash) throws IOException;

    /**
     * This method is used to add a new Review Request Transaction in pending transactions.
     * @param reviewRequest that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws IOException if there is an error in file Access
     */
    boolean submitPendingReviewRequest(ReviewRequestDTO reviewRequest) throws IOException;

    /**
     * This method is used to add a new Review Request Transaction in pending transactions.
     * @param finalDecision that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws IOException if there is an error in file Access
     */
    boolean submitPendingFinalDecision(FinalDecisionEntityDTO finalDecision) throws IOException;

    /**
     * This method is used to add a new Review Response Transaction in pending transactions.
     * @param reviewRequestEntity that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws IOException if there is an error in file Access
     */
    boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException;

    List<SubmitEntity> getReviewPendingArticles();

    List<SubmitEntity> getCurrentlyReviewingArticles();

    List<SubmitEntity> getReviewedArticles();

}
