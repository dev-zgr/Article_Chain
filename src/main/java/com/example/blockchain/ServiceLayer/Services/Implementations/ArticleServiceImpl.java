package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.DataLayer.Entities.*;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.FinalDecisionRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.ReviewRequestRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.SubmissionRepository;
import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewResponseLetterDTO;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Models.TransactionHolder;
import com.example.blockchain.ServiceLayer.Exceptions.NoSuchReviewRequest;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final BlockChainModel blockChainModel;
    private final BlockRepository blockRepository;
    private final SubmissionRepository submissionRepository;
    private final ReviewRequestRepository reviewRequestRepository;
    private final FinalDecisionRepository finalDecisionRepository;

    private final BlockChainService blockChainService;
    private final TransactionHolder transactionHolder;


    @Autowired
    public ArticleServiceImpl(BlockRepository blockRepository, BlockChainService blockChainService, SubmissionRepository submissionRepository, BlockChainModel blockChainModel, ReviewRequestRepository reviewRequestRepository, FinalDecisionRepository finalDecisionRepository, TransactionHolder transactionHolder) {
        this.blockRepository = blockRepository;
        this.blockChainService = blockChainService;
        this.submissionRepository = submissionRepository;
        this.blockChainModel = blockChainModel;
        this.reviewRequestRepository = reviewRequestRepository;
        this.finalDecisionRepository = finalDecisionRepository;
        this.transactionHolder = transactionHolder;
    }

    @Override
    public void submitArticle(ArticleEmbeddable articleEmbeddable) {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity();
        submitEntity.setArticle(articleEmbeddable);


        //get last block
        BlockEntity lastBlock = blockRepository.getBlockLastBlock();
        if (lastBlock.getTransactionList().size() >= blockChainModel.getMaxTransactionsPerBlock()) {


            //create new block
            BlockEntity recentBlock = blockChainService.mineBlock();
            recentBlock.getTransactionList().add(submitEntity);
            submitEntity.setMainBlock(recentBlock);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(recentBlock);

        } else {
            submitEntity.setMainBlock(lastBlock);
            lastBlock.getTransactionList().add(submitEntity);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(lastBlock);
        }
    }

    public boolean submitPendingSubmission(ArticleEmbeddable articleEmbeddable, String paperHash) throws IOException {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity();
        submitEntity.setArticle(articleEmbeddable);
        submitEntity.setPaper_hash(paperHash);

        return transactionHolder.addPendingTransaction(submitEntity);

    }

    @Override
    public boolean submitPendingReviewRequest(ReviewRequestDTO reviewRequest) throws IOException {
        //create review request entity
        ReviewRequestEntity reviewRequestEntity = new ReviewRequestEntity(
                reviewRequest.reviewerName,
                reviewRequest.reviewerResearchField,
                reviewRequest.reviewerEmail,
                reviewRequest.referringTxId);
        return transactionHolder.addPendingTransaction(reviewRequestEntity);
    }

    @Override
    public boolean submitFinalDecision(FinalDecisionEntityDTO finalDecision, long txId) throws NoSuchReviewRequest, IOException {
        Optional<ReviewRequestEntity> referringReviewRequest = reviewRequestRepository.findById(txId);
        if (referringReviewRequest.isEmpty()) {
            throw new NoSuchReviewRequest("There is no such review request");
        } else {
            FinalDecisionEntity finalDecisionEntity = new FinalDecisionEntity(
                    referringReviewRequest.get(),
                    finalDecision.decision_file_hash,
                    finalDecision.decisionPoint,
                    finalDecision.review_type,
                    finalDecision.review_hash
            );
            return transactionHolder.addPendingTransaction(finalDecisionEntity);
        }
    }

    @Override
    public boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException {
        ReviewResponseEntity reviewResponseEntity = new ReviewResponseEntity(
                reviewRequestEntity.reviewResponseLetterHash,
                reviewRequestEntity.referringSubmissionId
        );
        return transactionHolder.addPendingTransaction(reviewResponseEntity);
    }

    @Override
    public List<SubmitEntity> getReviewPendingArticles(String category, String title, String author, String department, String intuition, String keyword, Long tx_id) {
        List<Long> reviewPendingSubmissionIds = reviewRequestRepository.findReferringSubmissionIdsWithLessThanThreeOccurrences(category, title, author, department, intuition, keyword,tx_id);
        return reviewPendingSubmissionIds
                .stream()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmitEntity> getCurrentlyReviewingArticles() {
        /*
        List<Long> reviewPendingSubmissionIds = reviewRequestRepository.findReferringSubmissionIdsWithThreeOccurrences();
        return reviewPendingSubmissionIds
                .stream()
                .flatMap(id -> submissionRepository.getByTx_id(id).stream())
                .collect(Collectors.toList());

         */
        return null;
    }


    @Override
    public List<SubmitEntity> getVerifiedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId) {
        List<Long> reviewPendingSubmissionIds = finalDecisionRepository.findVerifiedSubmissions(category, title, author, department, intuition, keyword,txId);
        return reviewPendingSubmissionIds
                .stream()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .collect(Collectors.toList());
    }


    //TODO transaction merkle rootlari hesaplanacak
    //TODO private key public key ile sign edilecek
    @Override
    public List<SubmitEntity> getRejectedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId) {
        // THis is just returns the submissions that
        List<Long> firstRejectedSubmissionIds = finalDecisionRepository.findRejectedSubmissions(category,
                title,
                author,
                department,
                intuition,
                keyword,
                20,
                DecisionStatus.FirstReview,
                txId);
        List<Long> revisionRejectedSubmissionsId = finalDecisionRepository.findRejectedSubmissions(category,
                title,
                author,
                department,
                intuition,
                keyword,
                23,
                DecisionStatus.RevisionReview,
                txId);

        return Stream.concat(
                        firstRejectedSubmissionIds.stream(),
                        revisionRejectedSubmissionsId.stream())
                .distinct()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .toList();
    }


}
