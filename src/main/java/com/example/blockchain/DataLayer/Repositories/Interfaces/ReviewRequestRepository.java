package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.AcceptanceEnumDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling the operations of ReviewRequestEntity.
 */
@Repository
public interface ReviewRequestRepository extends JpaRepository<ReviewRequestEntity,Long> {
    /**
     * This method retries TX IDs of all review requests that have less than three occurrences in the database.
     * This method is used find TX IDs of submissions that have less than three reviews request.
     * TX ID's of these submissions refers to review pending submissions in the Article Chain.
     * @return A list of all review request entities in the database.
     */
    @Query("SELECT t.tx_id FROM SubmitEntity t WHERE t.tx_id NOT IN (" +
            "    SELECT r.manuscriptId " +
            "    FROM ReviewRequestEntity r " +
            "    WHERE r.acceptanceEnumDTO =  :acceptance" +
            "    GROUP BY r.manuscriptId " +
            "    HAVING COUNT(r.manuscriptId) >= 3" +
            ")" +
            "AND (:category IS NULL OR t.article.article_resField = :category) " +
            "AND (:title IS NULL OR t.article.article_title = :title) " +
            "AND (:author IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.author_name LIKE %:author%)) " +
            "AND (:department IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.department = :department)) " +
            "AND (:institution IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.institution = :institution)) " +
            "AND (:keyword IS NULL OR t.article.article_keywords LIKE %:keyword%) " +
            "AND (:txId IS NULL OR t.tx_id = :txId) "
            )
    List<Long> findReferringSubmissionIdsWithLessThanThreeOccurrences(
            @Param("category") String category,
            @Param("title") String title,
            @Param("author") String author,
            @Param("department") String department,
            @Param("institution") String institution,
            @Param("keyword") String keyword,
            @Param("txId") Long txId,
            @Param("acceptance")AcceptanceEnumDTO acceptanceEnumDTO
            );



    /**
     * This method retries TX IDs of all submissions that have three review request in the database.
     * Simply this method is used to find TX IDs of submissions that have three reviews request. In Article Chain
     * These transactions refers to currently reviewing submissions.
     * @return A list of all review request entities in the database.
     */
    @Query("SELECT t.manuscriptId FROM ReviewRequestEntity t GROUP BY t.manuscriptId HAVING COUNT(t) = 3")
    List<Long> findReferringSubmissionIdsWithThreeOccurrences();

}
