package com.example.blockchain.ServiceLayer.Services.Implementations.v2;

import com.example.blockchain.DataLayer.Entities.DecisionStatus;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.FinalDecisionRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.SubmissionRepository;
import com.example.blockchain.ServiceLayer.Services.Interfaces.V2.ArticleServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class ArticleServiceV2Impl implements ArticleServiceV2 {
    private final FinalDecisionRepository finalDecisionRepository;;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public ArticleServiceV2Impl(FinalDecisionRepository finalDecisionRepository, SubmissionRepository submissionRepository) {
        this.finalDecisionRepository = finalDecisionRepository;
        this.submissionRepository = submissionRepository;
    }


    @Override
    public List<SubmitEntity> getAllVerifiedSubmissions(int pageNo, boolean ascending) {
        // Fetch the verified submissions
        List<Long> verifiedSubmissionsFirstStep = finalDecisionRepository.findVerifiedSubmissions(
                null, null, null, null, null, 20, DecisionStatus.FirstReview, null, null, null);

        // Stream operations
        return verifiedSubmissionsFirstStep.stream()
                .distinct()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .map(s -> {
                    // Fetch the latest final decision date
                    String latestFinalDecisionDate = finalDecisionRepository.findLatestFinalDecisionDateByTxId(s.getTx_id());

                    if (latestFinalDecisionDate != null) {
                        LocalDateTime dateTime = LocalDateTime.parse(latestFinalDecisionDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        s.getArticle().setArticle_date(formattedDate);
                    }

                    return s;
                })
                .sorted(ascending ? Comparator.comparing(SubmitEntity::getTx_id) : Comparator.comparing(SubmitEntity::getTx_id).reversed()) // Adjust sorting based on ascending flag
                .skip(pageNo * 10L) // Adjusted to 10 items per page
                .limit(10)
                .toList();
    }


    @Override
    public long getPageCount() {
        List<Long> verifiedSubmissionsFirstStep = finalDecisionRepository.findVerifiedSubmissions(null, null, null, null, null,20, DecisionStatus.FirstReview ,null,null,null);
        return verifiedSubmissionsFirstStep.stream().distinct().flatMap(
                id -> submissionRepository.getByTxId(id).stream()
        ).map(s -> {
            String latestFinalDecisionDate = finalDecisionRepository.findLatestFinalDecisionDateByTxId(s.getTx_id());
            LocalDateTime dateTime = LocalDateTime.parse(latestFinalDecisionDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            s.getArticle().setArticle_date(formattedDate);
            return s;
        }).toList().size();
    }
}
