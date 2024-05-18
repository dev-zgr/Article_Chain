package com.example.blockchain.ServiceLayer.Services.Implementations.v2;

import com.example.blockchain.DataLayer.Entities.DecisionStatus;
import com.example.blockchain.DataLayer.Entities.ReviewRequestEntity;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.FinalDecisionRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.ReviewRequestRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.SubmissionRepository;
import com.example.blockchain.PresentationLayer.DataTransferObjects.AcceptanceEnumDTO;
import com.example.blockchain.PresentationLayer.DataTransferObjects.ReviewPendingArticleExtendedDTO;
import com.example.blockchain.ServiceLayer.Mappers.SubmissionMapper;
import com.example.blockchain.ServiceLayer.Services.Interfaces.V2.ArticleServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceV2Impl implements ArticleServiceV2 {
    private final FinalDecisionRepository finalDecisionRepository;;
    private final SubmissionRepository submissionRepository;
    private final ReviewRequestRepository reviewRequestRepository;

    @Autowired
    public ArticleServiceV2Impl(FinalDecisionRepository finalDecisionRepository, SubmissionRepository submissionRepository, ReviewRequestRepository reviewRequestRepository) {
        this.finalDecisionRepository = finalDecisionRepository;
        this.submissionRepository = submissionRepository;
        this.reviewRequestRepository = reviewRequestRepository;
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
    public long getPageCountVerifiedArticle() {
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

    @Override
    public List<ReviewPendingArticleExtendedDTO> getAcceptedReviewByEmailSubmissions(String email, int pageNo, boolean ascending) {

        List<ReviewRequestEntity> pendingSubmissionIds = finalDecisionRepository.findPendingReviewsByEmail(email, AcceptanceEnumDTO.ACCEPTED);
        List<ReviewPendingArticleExtendedDTO> articles = new java.util.ArrayList<>(pendingSubmissionIds.stream()
                .distinct()
                .<ReviewPendingArticleExtendedDTO>flatMap(reviewRequest -> {
                    SubmitEntity submitEntity = submissionRepository.getByTxId(reviewRequest.getManuscriptId()).get(0);
                    return Stream.of(new ReviewPendingArticleExtendedDTO(
                            SubmissionMapper.mapArticleEmbeddableToDTO(submitEntity.getArticle()),
                            submitEntity.getPaper_hash(),
                            reviewRequest.getTx_id()
                    ));
                })
                .toList());


        if (ascending) {
            articles.sort(Comparator.comparing(ReviewPendingArticleExtendedDTO::getReferringReviewTXID));
        } else {
            articles.sort(Comparator.comparing(ReviewPendingArticleExtendedDTO::getReferringReviewTXID).reversed());
        }

        // Pagination
        int pageSize = 10;
        int startIdx = pageNo * pageSize;
        int endIdx = Math.min(startIdx + pageSize, pendingSubmissionIds.size());
        return articles.subList(startIdx, endIdx);
    }

    @Override
    public ReviewPendingArticleExtendedDTO getAcceptedReviewByEmailSubmissionsAndTXID(String email, long tx_id) {

        List<ReviewRequestEntity> pendingSubmissionIds = finalDecisionRepository.findPendingReviewsByEmail(email, AcceptanceEnumDTO.ACCEPTED);

        // Retrieve the ReviewPendingArticleExtendedDTO based on the provided tx_id
        Optional<ReviewPendingArticleExtendedDTO> matchingArticle = pendingSubmissionIds.stream()
                .distinct()
                .flatMap(reviewRequest -> {
                    SubmitEntity submitEntity = submissionRepository.getByTxId(reviewRequest.getManuscriptId()).get(0);
                    return Stream.of(new ReviewPendingArticleExtendedDTO(
                            SubmissionMapper.mapArticleEmbeddableToDTO(submitEntity.getArticle()),
                            submitEntity.getPaper_hash(),
                            reviewRequest.getTx_id()
                    ));
                })
                .filter(reviewPendingArticleExtendedDTO -> reviewPendingArticleExtendedDTO.getReferringReviewTXID() == tx_id)
                .findFirst();

        // Return the matching ReviewPendingArticleExtendedDTO if found, otherwise return null
        return matchingArticle.orElse(null);
    }

    @Override
    public long getPageCountAcceptedReviewByEmail(String email) {
        try {
            // Fetch the list of pending submission IDs for the provided email with acceptance status ACCEPTED
            List<ReviewRequestEntity> pendingSubmissionIds = finalDecisionRepository.findPendingReviewsByEmail(email, AcceptanceEnumDTO.ACCEPTED);

            // Extract unique submission IDs
            Set<Long> uniqueSubmissionIds = pendingSubmissionIds.stream()
                    .map(ReviewRequestEntity::getManuscriptId)
                    .collect(Collectors.toSet());

            // Calculate the total number of pages based on the number of unique submission IDs
            long totalCount = uniqueSubmissionIds.size();
            int pageSize = 10; // Assuming 10 items per page
            return (totalCount + pageSize - 1) / pageSize; // Calculate the total number of pages
        } catch (Exception e) {
            // Handle any exceptions or return a default value
            return 0;
        }
    }


}
