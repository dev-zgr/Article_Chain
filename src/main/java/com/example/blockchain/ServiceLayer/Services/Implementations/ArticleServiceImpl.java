package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.DataLayer.Entities.*;
import com.example.blockchain.DataLayer.Repositories.Interfaces.*;
import com.example.blockchain.PresentationLayer.DataTransferObjects.*;
import com.example.blockchain.ServiceLayer.Mappers.SubmissionMapper;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Models.NodeModel;
import com.example.blockchain.ServiceLayer.Models.TransactionHolder;
import com.example.blockchain.ServiceLayer.Exceptions.NoSuchReviewRequest;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Value("${pagination.maxPageSize}")
    private int pageSize;
    private final BlockChainModel blockChainModel;
    private final BlockRepository blockRepository;
    private final SubmissionRepository submissionRepository;
    private final ReviewRequestRepository reviewRequestRepository;
    private final FinalDecisionRepository finalDecisionRepository;
    private final BlockChainService blockChainService;
    private final TransactionHolder transactionHolder;

    private final BlockChainPagingSortingRepository blockChainPagingSortingRepository;
    private final NodeModel nodeModel;

    @Value("${node.addressingSystem.ip}")
    private String nrsIpAddress;

    @Value("${node.addressingSystem.port}")
    private String nrsPort;

    @Autowired
    public ArticleServiceImpl(BlockRepository blockRepository, BlockChainService blockChainService, SubmissionRepository submissionRepository, BlockChainModel blockChainModel, ReviewRequestRepository reviewRequestRepository, FinalDecisionRepository finalDecisionRepository, TransactionHolder transactionHolder, BlockChainPagingSortingRepository blockChainPagingSortingRepository, NodeModel nodeModel) {
        this.blockRepository = blockRepository;
        this.blockChainService = blockChainService;
        this.submissionRepository = submissionRepository;
        this.blockChainModel = blockChainModel;
        this.reviewRequestRepository = reviewRequestRepository;
        this.finalDecisionRepository = finalDecisionRepository;
        this.transactionHolder = transactionHolder;
        this.blockChainPagingSortingRepository = blockChainPagingSortingRepository;
        this.nodeModel = nodeModel;
    }

    @Override
    public void submitArticle(ArticleEmbeddable articleEmbeddable) {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity(nodeModel.getUuid());
        submitEntity.setArticle(articleEmbeddable);


        //get last block
        BlockEntity lastBlock = blockRepository.getBlockLastBlock();
        if (lastBlock.getTransactionList().size() >= blockChainModel.getMaxTransactionsPerBlock()) {


            //create new block
            BlockEntity recentBlock = blockChainService.mineBlock();
            recentBlock.getTransactionList().add(submitEntity);
            submitEntity.setMainBlock(recentBlock);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(recentBlock);

        } else {
            submitEntity.setMainBlock(lastBlock);
            lastBlock.getTransactionList().add(submitEntity);
            submissionRepository.save(submitEntity);
            blockRepository.updateBlock(lastBlock);
        }
    }

    public boolean submitPendingSubmission(ArticleEmbeddableDTO articleEmbeddable, MultipartFile multipartFile, String paperHash) throws IOException {
        //create submission entity
        SubmitEntity submitEntity = new SubmitEntity(nodeModel.getUuid());
        ArticleEmbeddable articleEmbeddable1 = SubmissionMapper.mapDTOToArticleEmbeddable(articleEmbeddable, new ArticleEmbeddable());
        submitEntity.setArticle(articleEmbeddable1);
        System.out.println(articleEmbeddable1);
        submitEntity.setPaper_hash(paperHash);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileData(multipartFile.getBytes());
        fileEntity.setFileIdentifier(submitEntity.getArticle().getFileIdentifier());
        boolean filePosted =  postFile(fileEntity);
        if(!filePosted){
            return  false;
        }
        return transactionHolder.addPendingTransaction(submitEntity);

    }

    @Override
    public boolean submitPendingReviewRequest(ReviewRequestDTO reviewRequest) throws IOException {
        //create review request entity
        ReviewRequestEntity reviewRequestEntity = new ReviewRequestEntity(
                reviewRequest.reviewerName,
                reviewRequest.reviewerResearchField,
                reviewRequest.reviewerEmail,
                reviewRequest.referringTxId,
                reviewRequest.acceptanceStatus,
                nodeModel.getUuid());
        return transactionHolder.addPendingTransaction(reviewRequestEntity);
    }

    @Override
    public boolean submitFinalDecision(FinalDecisionEntityDTO finalDecision,MultipartFile multipartFile ,long txId) throws NoSuchReviewRequest, IOException {
        Optional<ReviewRequestEntity> referringReviewRequest = reviewRequestRepository.findById(txId);
        if (referringReviewRequest.isEmpty()) {
            throw new NoSuchReviewRequest("There is no such review request");
        } else {
            FinalDecisionEntity finalDecisionEntity = new FinalDecisionEntity(
                    referringReviewRequest.get(),
                    finalDecision.decision_file_hash,
                    finalDecision.decisionPoint,
                    finalDecision.review_type,
                    AcceptanceEnumDTO.ACCEPTED,
                    finalDecision.getFileIdentifier(),
                    nodeModel.getUuid()
            );

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileData(multipartFile.getBytes());
            fileEntity.setFileIdentifier(finalDecisionEntity.getFileIdentifier());
            boolean filePosted =  postFile(fileEntity);
            if(!filePosted){
                return  false;
            }
            return transactionHolder.addPendingTransaction(finalDecisionEntity);
        }
    }

//    @Override
//    public boolean submitPendingReviewResponse(ReviewResponseLetterDTO reviewRequestEntity) throws IOException {
//        ReviewResponseEntity reviewResponseEntity = new ReviewResponseEntity(
//                reviewRequestEntity.reviewResponseLetterHash,
//                reviewRequestEntity.referringSubmissionId
//        );
//        return transactionHolder.addPendingTransaction(reviewResponseEntity);
//    }

    @Override
    public List<SubmitEntity> getReviewPendingArticles(String category, String title, String author, String department, String intuition, String keyword, Long tx_id) {
        List<Long> reviewPendingSubmissionIds = reviewRequestRepository.findReferringSubmissionIdsWithLessThanThreeOccurrences(category, title, author, department, intuition, keyword,tx_id, AcceptanceEnumDTO.ACCEPTED);
        return reviewPendingSubmissionIds
                .stream()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmitEntity> getCurrentlyReviewingArticles() {
        /*
        List<Long> reviewPendingSubmissionIds = reviewRequestRepository.findReferringSubmissionIdsWithThreeOccurrences();
        return reviewPendingSubmissionIds
                .stream()
                .flatMap(id -> submissionRepository.getByTx_id(id).stream())
                .collect(Collectors.toList());

         */
        return null;
    }


    @Override
    public List<SubmitEntity> getVerifiedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId,String articleType) {
        List<Long> verifiedSubmissionsFirstStep = finalDecisionRepository.findVerifiedSubmissions(category, title, author, department, intuition,20,DecisionStatus.FirstReview ,keyword,txId,articleType);
        return verifiedSubmissionsFirstStep.stream().distinct().flatMap(
                id -> submissionRepository.getByTxId(id).stream()
        ).map(s -> {
            String latestFinalDecisionDate = finalDecisionRepository.findLatestFinalDecisionDateByTxId(s.getTx_id());
            LocalDateTime dateTime = LocalDateTime.parse(latestFinalDecisionDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            s.getArticle().setArticle_date(formattedDate);
            return s;
        }).toList();
    }

    @Override
    public List<SubmitEntity> getRejectedSubmissions(String category, String title, String author, String department, String intuition, String keyword, Long txId) {
        // THis is just returns the submissions that
        List<Long> firstRejectedSubmissionIds = finalDecisionRepository.findRejectedSubmissions(category,
                title,
                author,
                department,
                intuition,
                keyword,
                20,
                DecisionStatus.FirstReview,
                txId);
        List<Long> revisionRejectedSubmissionsId = finalDecisionRepository.findRejectedSubmissions(category,
                title,
                author,
                department,
                intuition,
                keyword,
                23,
                DecisionStatus.RevisionReview,
                txId);

        return Stream.concat(
                        firstRejectedSubmissionIds.stream(),
                        revisionRejectedSubmissionsId.stream())
                .distinct()
                .flatMap(id -> submissionRepository.getByTxId(id).stream())
                .toList();
    }

    @Override
    public List<ReviewPendingArticleExtendedDTO> getAcceptedReviewByEmailSubmissions(String email) {
        List<ReviewRequestEntity> pendingSubmissionIds = finalDecisionRepository.findPendingReviewsByEmail(email, AcceptanceEnumDTO.ACCEPTED);
        return pendingSubmissionIds.stream()
                .distinct()
                .flatMap(reviewRequest -> {
                    SubmitEntity submitEntity = submissionRepository.getByTxId(reviewRequest.getManuscriptId()).get(0);
                    return Stream.of(new ReviewPendingArticleExtendedDTO(
                            SubmissionMapper.mapArticleEmbeddableToDTO(submitEntity.getArticle()),
                            submitEntity.getPaper_hash(),
                            reviewRequest.getTx_id()
                    ));
                })
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getFileByUUID(UUID filenameUUID) {
        String baseUrl = "http://" + nrsIpAddress + ":" + nrsPort + "/file?uuid=" + filenameUUID;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(baseUrl, byte[].class);
        return response.getBody();


    }

    @Override
    public SubmitEntity getPendingTransactionById(Long txId) {
        return submissionRepository.getByTxId(txId).get(0);
    }

    @Override
    public List<BlockEntity> getAllBlock(int pageNo, boolean ascending) {
        Pageable pageable;
        if (ascending) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("indexNo").ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("indexNo").descending());
        }
        return blockChainPagingSortingRepository.findAll(pageable).getContent();
    }

    @Override
    public Long getBlockPageCount() {
        return (blockChainPagingSortingRepository.count() + pageSize - 1) / pageSize;
    }


    private boolean postFile(FileEntity file){

        String baseUrl = "http://" + nrsIpAddress + ":" + nrsPort + "/file";
        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl, file, String.class);
            return responseEntity.getStatusCode().is2xxSuccessful();

        }catch (Exception e){
            return false;
        }
    }
}
