package com.example.blockchain.ServiceLayer.Services.Interfaces;

import com.example.blockchain.DataLayer.Entities.*;
import com.example.blockchain.PresentationLayer.DataTransferObjects.*;
import com.example.blockchain.ServiceLayer.Exceptions.NoSuchReviewRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    boolean submitPendingSubmission(ArticleEmbeddableDTO articleEmbeddable, MultipartFile multipartFile, String paperHash) throws IOException;

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
    boolean submitFinalDecision(FinalDecisionEntityDTO finalDecisionEntity,MultipartFile multiPartFile,long txId) throws NoSuchReviewRequest,IOException;


//    boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException;

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

    List<SubmitEntity> getVerifiedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId,String articleType);

    List<SubmitEntity> getRejectedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId);

    /**
     * This method is used to fetch the submissions that are accepted but not reviewed by the user. So this may be used
     * to show the user the submissions that are accepted and are pending for review.
     * @param email The email of the user.
     * @return List of accepted submissions that are pending for review.
     */
    List<ReviewPendingArticleExtendedDTO> getAcceptedReviewByEmailSubmissions(String email);


    /**
     * This method is used to fetch the submissions that are rejected but not reviewed by the user. So this may be used
     * To show the user the submissions that are rejected and are pending for review.
     * @param filenameUUID UUID of the file that'll be retrieved
     * @return Resource of the file
     */
    byte[] getFileByUUID(UUID filenameUUID);

    /**
     * Gets the pending transaction by its ID
     * @param id ID of the transaction
     * @return TransactionEntity if exists
     */
    SubmitEntity getPendingTransactionById(Long txId);

    List<BlockEntity> getAllBlock(int pageNo, boolean ascending);
    Long getBlockPageCount();

}
