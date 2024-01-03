package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.DataLayer.Entities.*;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.SubmissionRepository;
import com.example.blockchain.PresentationLayer.DataTransferObjects.FinalDecisionEntityDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewRequestDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewResponseLetterDTO;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Models.TransactionHolder;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final BlockChainModel blockChainModel;
    private final BlockRepository blockRepository;
    private final SubmissionRepository submissionRepository;
    private final BlockChainService blockChainService;
    private final TransactionHolder transactionHolder;


    @Autowired
    public ArticleServiceImpl(BlockRepository blockRepository, BlockChainService blockChainService, SubmissionRepository submissionRepository, BlockChainModel blockChainModel, TransactionHolder transactionHolder) {
        this.blockRepository = blockRepository;
        this.blockChainService = blockChainService;
        this.submissionRepository =submissionRepository;
        this.blockChainModel = blockChainModel;
        this.transactionHolder = transactionHolder;
    }

    @Override
    public void submitArticle(ArticleEmbeddable articleEmbeddable) {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity();
        submitEntity.setArticle(articleEmbeddable);


        //get last block
        BlockEntity lastBlock = blockRepository.getBlockLastBlock();
        if(lastBlock.getTransactionList().size() >= blockChainModel.getMaxTransactionsPerBlock()){


            //create new block
            BlockEntity  recentBlock= blockChainService.mineBlock();
            recentBlock.getTransactionList().add(submitEntity);
            submitEntity.setMainBlock(recentBlock);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(recentBlock);

        }else {
            submitEntity.setMainBlock(lastBlock);
            lastBlock.getTransactionList().add(submitEntity);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(lastBlock);
        }
    }

    public boolean submitPendingSubmission(ArticleEmbeddable articleEmbeddable, String paperHash) throws IOException{
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity();
        submitEntity.setArticle(articleEmbeddable);
        submitEntity.setPaper_hash(paperHash);

        return transactionHolder.addPendingTransaction(submitEntity);

    }

    @Override
    public boolean submitPendingReviewRequest(ReviewRequestDTO reviewRequest)throws IOException {
        //create review request entity
        ReviewRequestEntity reviewRequestEntity = new ReviewRequestEntity(
                reviewRequest.reviewerName,
                reviewRequest.reviewerResearchField,
                reviewRequest.reviewerEmail,
                reviewRequest.referringSubmissionId);
        return transactionHolder.addPendingTransaction(reviewRequestEntity);
    }

    @Override
    public boolean submitPendingFinalDecision(FinalDecisionEntityDTO finalDecision) throws IOException {
            FinalDecisionEntity finalDecisionEntity = new FinalDecisionEntity(
                    new ReviewRequestEntity(
                            finalDecision.reviewRequestEntityDTO.reviewerName,
                            finalDecision.reviewRequestEntityDTO.reviewerResearchField,
                            finalDecision.reviewRequestEntityDTO.reviewerEmail,
                            finalDecision.reviewRequestEntityDTO.referringSubmissionId),
                    finalDecision.decision_file_hash,
                    finalDecision.decisionPoint, DecisionStatus.FirstReview
            );
            finalDecisionEntity.setReview_hash(BlockEntity.calculateHash(finalDecisionEntity.toString()));
        return transactionHolder.addPendingTransaction(finalDecisionEntity);
    }

    @Override
    public boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException {
        ReviewResponseEntity reviewResponseEntity = new ReviewResponseEntity(
                reviewRequestEntity.reviewResponseLetterHash,
                reviewRequestEntity.referringSubmissionId
        );
        return transactionHolder.addPendingTransaction(reviewResponseEntity);
    }


}
