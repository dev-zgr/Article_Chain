package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.DecisionStatus;
import com.example.blockchain.DataLayer.Entities.FinalDecisionEntity;
import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.AcceptanceEnumDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface FinalDecisionRepository extends JpaRepository<FinalDecisionEntity,Long> {
    @Query("SELECT t.tx_id FROM SubmitEntity t WHERE t.tx_id IN (                   " +
            "       SELECT r.manuscriptId                                          " +
            "       FROM FinalDecisionEntity r                                      " +
            "       WHERE r.review_type = :reviewType                          "+
            "       GROUP BY r.manuscriptId                                        " +
            "       HAVING SUM(r.decisionPoint) >= :requiredPoint)                              " +
            "AND (:category IS NULL OR t.article.article_resField = :category)      " +
            "AND (:title IS NULL OR t.article.article_title = :title)               " +
            "AND (:author IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.author_name LIKE %:author%)) " +
            "AND (:department IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.department = :department)) " +
            "AND (:institution IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.institution = :institution)) " +
            "AND (:keyword IS NULL OR t.article.article_keywords LIKE %:keyword%)   " +
            "AND (:txId IS NULL OR t.tx_id = :txId)                                  " +
            "AND (:article_type IS NULL OR t.article.article_keywords = :article_type)   ")
    List<Long> findVerifiedSubmissions(
            @Param("category") String category,
            @Param("title") String title,
            @Param("author") String author,
            @Param("department") String department,
            @Param("institution") String institution,
            @Param("requiredPoint") int RequiredPoint,
            @Param("reviewType") DecisionStatus decisionStatus,
            @Param("keyword") String keyword,
            @Param("txId") Long txId,
            @Param("article_type") String articleType
    );



    @Query("SELECT t.tx_id FROM SubmitEntity t WHERE t.tx_id IN (               " +
            "       SELECT r.manuscriptId                                      " +
            "       FROM FinalDecisionEntity r " +
            "       WHERE r.review_type = :reviewType                          "+
            "       GROUP BY r.manuscriptId " +
            "       HAVING SUM(r.decisionPoint) < :requiredPoint ) " +
            "AND (:category IS NULL OR t.article.article_resField = :category) " +
            "AND (:title IS NULL OR t.article.article_title = :title) " +
            "AND (:author IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.author_name LIKE %:author%)) " +
            "AND (:department IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.department = :department)) " +
            "AND (:institution IS NULL OR EXISTS (SELECT a FROM AuthorEntity a WHERE a.institution = :institution)) " +
            "AND (:keyword IS NULL OR t.article.article_keywords LIKE %:keyword%) " +
            "AND (:txId IS NULL OR t.tx_id = :txId) ")
    List<Long> findRejectedSubmissions(
            @Param("category") String category,
            @Param("title") String title,
            @Param("author") String author,
            @Param("department") String department,
            @Param("institution") String institution,
            @Param("keyword") String keyword,
            @Param("requiredPoint") int RequiredPoint,
            @Param("reviewType") DecisionStatus decisionStatus,
            @Param("txId") Long txId
    );

    @Query(
            "SELECT t.timestamp FROM FinalDecisionEntity t WHERE t.manuscriptId = :tx_id ORDER BY t.timestamp DESC LIMIT 1"
    )
    String findLatestFinalDecisionDateByTxId(@Param("tx_id") Long txId);


    @Query(" SELECT distinct r.manuscriptId                                     " +
            "FROM ReviewRequestEntity r                                         " +
            "WHERE r.acceptanceEnumDTO = :acceptance                            " +
            "   AND r.reviewer_email = :email                                   " +
            "   AND r.manuscriptId NOT IN (                                     " +
            "      SELECT DISTINCT f.manuscriptId                               " +
            "      FROM FinalDecisionEntity f                                   " +
            "      WHERE f.reviewer_email = :email                              " +
            ")")
    List<Long> findPendingReviewsManuscriptIdByEmail(
            @Param("email") String email,
            @Param("acceptance")AcceptanceEnumDTO acceptanceEnumDTO);



    @Query(" SELECT distinct r                                  " +
            "FROM ReviewRequestEntity r                                         " +
            "WHERE r.acceptanceEnumDTO = :acceptance                            " +
            "   AND r.reviewer_email = :email                                   " +
            "   AND r.manuscriptId NOT IN (                                     " +
            "      SELECT DISTINCT f.manuscriptId                               " +
            "      FROM FinalDecisionEntity f                                   " +
            "      WHERE f.reviewer_email = :email                              " +
            ")")
    List<ReviewRequestEntity> findPendingReviewsByEmail(
            @Param("email") String email,
            @Param("acceptance")AcceptanceEnumDTO acceptanceEnumDTO);

}
