package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.DecisionStatus;
import com.example.blockchain.DataLayer.Entities.FinalDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface FinalDecisionRepository extends JpaRepository<FinalDecisionEntity,Long> {
    @Query("SELECT t.tx_id FROM SubmitEntity t WHERE t.tx_id IN (                   " +
            "       SELECT r.referringTxId                                          " +
            "       FROM FinalDecisionEntity r                                      " +
            "       GROUP BY r.referringTxId                                        " +
            "       HAVING SUM(r.decisionPoint) >= 25)                              " +
            "AND (:category IS NULL OR t.article.article_resField = :category)      " +
            "AND (:title IS NULL OR t.article.article_title = :title)               " +
            "AND (:author IS NULL OR t.article.author_name LIKE %:author%)          " +
            "AND (:department IS NULL OR t.article.department = :department)        " +
            "AND (:institution IS NULL OR t.article.institution = :institution)     " +
            "AND (:keyword IS NULL OR t.article.article_keywords LIKE %:keyword%)   " +
            "AND (:txId IS NULL OR t.tx_id = :txId)                                 ")
    List<Long> findVerifiedSubmissions(
            @Param("category") String category,
            @Param("title") String title,
            @Param("author") String author,
            @Param("department") String department,
            @Param("institution") String institution,
            @Param("keyword") String keyword,
            @Param("txId") Long txId
    );



    @Query("SELECT t.tx_id FROM SubmitEntity t WHERE t.tx_id IN (               " +
            "       SELECT r.referringTxId                                      " +
            "       FROM FinalDecisionEntity r " +
            "       WHERE r.review_type = :reviewType                          "+
            "       GROUP BY r.referringTxId " +
            "       HAVING SUM(r.decisionPoint) < :requiredPoint ) " +
            "AND (:category IS NULL OR t.article.article_resField = :category) " +
            "AND (:title IS NULL OR t.article.article_title = :title) " +
            "AND (:author IS NULL OR t.article.author_name LIKE %:author%) " +
            "AND (:department IS NULL OR t.article.department = :department) " +
            "AND (:institution IS NULL OR t.article.institution = :institution) " +
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
}
