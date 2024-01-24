package com.example.blockchain.DataLayer.Repositories.Interfaces;


import com.example.blockchain.DataLayer.Entities.BlockEntity;

import java.util.List;

/**
 * Repository interface for handling the operations of BlockEntity.
 */
public interface BlockRepository {

    /**
     * Adds a new block in to the blockchain.
     *
     * @param blockEntity The block entity to be persisted.
     * @return True if the block was successfully persisted, false otherwise.
     */
    //Create
    boolean persistBlock(BlockEntity blockEntity);

    /**
     * Retrieves a block from the blockchain by its index.
     *
     * @param index The index of the block to be retrieved.
     * @return The block entity with the specified index.
     */
    // Read operations
    BlockEntity getBlockByIndex(int index);

    /**
     * Retrieves all blocks in the blockchain.
     *
     * @return A list of all block entities in the blockchain.
     */
    List<BlockEntity> getBlockAllBlock();

    /**
     * Retrieves the last block in the blockchain.
     *
     * @return The last block entity in the blockchain.
     */
    BlockEntity getBlockLastBlock();

    /**
     * Retrieves the last two blocks in the blockchain.
     *
     * @return A list containing the last two block entities in the blockchain.
     */
    List<BlockEntity> getLastTwoBlock();

    /**
     * Removes a block from the blockchain.
     *
     * @param blockEntity The block entity to be removed.
     * @return True if the block was successfully removed, false otherwise.
     */
    boolean removeBlock(BlockEntity blockEntity);

    /**
     * Removes a block from the blockchain based on its index.
     *
     * @param index The index of the block to be removed.
     * @return True if the block was successfully removed, false otherwise.
     */
    boolean removeBlockByIndex(int index);

    /**
     * Retrieves the last index in the blockchain.
     *
     * @return The last index in the blockchain.
     */
    int getLastIndex();

    /**
     * Deletes all blocks in the blockchain.
     */
    void deleteAllBlocks();

    /**
     * Updates the information of a block.
     *
     * @param blockEntity The block entity with updated information.
     * @return True if the block was successfully updated, false otherwise.
     */
    boolean updateBlock(BlockEntity blockEntity) ;

    }
