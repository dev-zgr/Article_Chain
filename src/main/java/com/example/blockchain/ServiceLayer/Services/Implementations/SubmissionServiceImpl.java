package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.SubmissionRepository;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Services.BlockChainService;
import com.example.blockchain.ServiceLayer.Services.Interfaces.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final BlockRepository blockRepository;
    private final BlockChainService blockChainService;
    private final SubmissionRepository submissionRepository;
    private final BlockChainModel blockChainModel;


    @Autowired
    public SubmissionServiceImpl(BlockRepository blockRepository, BlockChainService blockChainService, SubmissionRepository submissionRepository, BlockChainModel blockChainModel) {
        this.blockRepository = blockRepository;
        this.blockChainService = blockChainService;
        this.submissionRepository =submissionRepository;
        this.blockChainModel = blockChainModel;
    }

    @Override
    public void submitArticle(ArticleEmbeddable articleEmbeddable) {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity();
        submitEntity.setArticle(articleEmbeddable);
        //TODO : burayi duzelt
        submitEntity.setPaper_hash(null);

        //TODO : burayi duzelt
        submitEntity.setAbstract_hash(null);

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



}
