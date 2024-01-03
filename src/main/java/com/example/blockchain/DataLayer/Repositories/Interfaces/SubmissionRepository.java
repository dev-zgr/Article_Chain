package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import org.springframework.cglib.core.Block;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * @return A list of submission entities associated with the specified block.
     */
    List<SubmitEntity> getAllByMainBlock(BlockEntity blockEntity);

}
