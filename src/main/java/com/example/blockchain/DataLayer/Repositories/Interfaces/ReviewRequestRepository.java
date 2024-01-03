package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRequestRepository extends JpaRepository<ReviewRequestEntity,Long> {
    @Query("SELECT t.referringTxId FROM ReviewRequestEntity t GROUP BY t.referringTxId HAVING COUNT(t) < 3")
    List<Long> findReferringSubmissionIdsWithLessThanThreeOccurrences();

    @Query("SELECT t.referringTxId FROM ReviewRequestEntity t GROUP BY t.referringTxId HAVING COUNT(t) = 3")
    List<Long> findReferringSubmissionIdsWithThreeOccurrences();

}
