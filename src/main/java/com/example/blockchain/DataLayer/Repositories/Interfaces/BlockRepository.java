package com.example.blockchain.DataLayer.Repositories.Interfaces;


import com.example.blockchain.DataLayer.Entities.BlockEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BlockRepository {

    //Create
    boolean persistBlock(BlockEntity blockEntity);

    // Read operations
    BlockEntity getBlockByIndex(int index);

    List<BlockEntity> getBlockAllBlock();

    BlockEntity getBlockLastBlock();

    List<BlockEntity> getLastTwoBlock();

    boolean removeBlock(BlockEntity blockEntity);
    boolean removeBlockByIndex(int index);

    int getLastIndex();


    void deleteAllBlocks();
}
