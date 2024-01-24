package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for handling database operations of SubmitEntity.
 */
@Repository
public interface SubmissionRepository extends JpaRepository<SubmitEntity, Long> {

    /**
     * Retrieves all submission entities associated with a specific block.
     *
     * @param blockEntity The block entity to which submissions are associated.
     * @return A list of SubmitEntity associated with the specified block.
     */
    List<SubmitEntity> getAllByMainBlock(BlockEntity blockEntity);

    /**
     * Retrieves submission entities by their transaction ID.
     *
     * @param txId The transaction ID associated with the submissions.
     * @return A list of SubmitEntity associated with the specified transaction ID.
     */
    @Query("SELECT s FROM SubmitEntity s WHERE s.tx_id = :txId")
    List<SubmitEntity> getByTxId(@Param("txId") long txId);


}
