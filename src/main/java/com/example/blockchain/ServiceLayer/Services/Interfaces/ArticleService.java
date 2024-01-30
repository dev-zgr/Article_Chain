package com.example.blockchain.ServiceLayer.Services.Interfaces;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewResponseLetterDTO;
import com.example.blockchain.ServiceLayer.Exceptions.NoSuchReviewRequest;

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
     */
    void submitArticle(ArticleEmbeddable articleEmbeddable);

    /**
     * This method is used to add a new Submission Transaction in pending transactions.
     * @param articleEmbeddable that transaction will be added to pending transaction
     * @param paperHash that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws IOException if there is an error in file Access
     */
    boolean submitPendingSubmission(ArticleEmbeddable articleEmbeddable, String paperHash) throws IOException;

    /**
     * This method is used to add a new Review Request Transaction in pending transactions.
     * @param reviewRequest that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws IOException if there is an error in file Access
     */
    boolean submitPendingReviewRequest(ReviewRequestDTO reviewRequest) throws IOException;



    /**
     * This method is used to add a new Review Response Transaction in pending transactions.
     * @param finalDecision that transaction will be added to pending transaction
     * @return save status true if saved successfully, false if not
     * @throws NoSuchReviewRequest if there is an error in file Access
     */
    boolean submitFinalDecision(FinalDecisionEntityDTO finalDecision, long txId) throws NoSuchReviewRequest,IOException;


    boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException;

    /**
     * Returns the currently reviewing articles by following criteria
     * @param category of article
     * @param title of article
     * @param author of article
     * @param department of article
     * @param intuition of article
     * @param keyword of article
     * @return list of desired pending articles
     */
    List<SubmitEntity> getReviewPendingArticles(String category, String title, String author, String department, String intuition, String keyword, Long tx_id);

    /**
     * This method is used to get all currently reviewing transactions.
     * @return List of  currently reviewing submission transaction that three review request transaction
     * but with no final decision transaction
     */
    List<SubmitEntity> getCurrentlyReviewingArticles();

    List<SubmitEntity> getVerifiedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId);

    List<SubmitEntity> getRejectedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId);
}
