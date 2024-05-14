package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.NodeRecord;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import com.example.blockchain.PresentationLayer.DataTransferObjects.GenericListResponseDTO;
import com.example.blockchain.ServiceLayer.Models.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing the blockchain model
 */
@Service
public class BlockChainService {
    /**
     * This is the repository that'll be used to store the blocks
     */
    private final BlockRepository blockRepository;
    /**
     * This is the transaction holder that'll be used to store the transactions
     */
    private final TransactionHolder transactionHolder;

    /**
     * This is the blockchain model that'll be used to validate the blocks
     */
    private final BlockChainModel blockChainModel;

    /**
     * This is the transaction repository that'll be used to store the transactions
     */
    private final TransactionRepository transactionRepository;


    /**
     * This is the node addressing system model that'll be used to communicate with other nodes
     */
    private final NodeAdressingSystemModel nodeAdressingSystemModel;

    /**
     * This is the node model that'll be used to get the node's information
     */
    private final NodeModel nodeModel;

    /**
     * This is the digital signature key manager of the current node
     */
    private final KeyHolder keyHolder;

    /**
     * This constructor initializes the block repository, transaction holder, blockchain model, transaction repository, node addressing system model and node model
     * @param blockRepository This is the repository that'll be used to store the blocks
     * @param transactionHolder This is the transaction holder that'll be used to store the transactions
     * @param blockChainModel This is the blockchain model that'll be used to validate the blocks
     * @param transactionRepository This is the transaction repository that'll be used to store the transactions
     * @param nodeAdressingSystemModel This is the node addressing system model that'll be used to communicate with other nodes
     * @param nodeModel This is the node model that'll be used to get the node's information
     */
    @Autowired
    public BlockChainService(BlockRepository blockRepository, TransactionHolder transactionHolder, BlockChainModel blockChainModel, TransactionRepository transactionRepository, NodeAdressingSystemModel nodeAdressingSystemModel, NodeModel nodeModel, KeyHolder keyHolder) {
        this.blockRepository = blockRepository;
        this.transactionHolder = transactionHolder;
        this.blockChainModel = blockChainModel;
        this.transactionRepository = transactionRepository;
        this.nodeAdressingSystemModel = nodeAdressingSystemModel;
        this.nodeModel = nodeModel;
        this.keyHolder = keyHolder;
    }


    /**
     * This method initializes the blockchain model Performs the following operations:
     * 1) Register the node to node recording system
     * 2) Get all nodes from node recording system
     * 3) Filter active nodes
     * 4) If there is only one active node and there is no block in the chain, create a genesis block
     */
    @PostConstruct
    public void init() {
        // Load the digital signature keys of the current node
        keyHolder.loadKeys("12345678", "MyKeyPair", "my_keypair.jks");

        // First register to node recording System
        NodeRecord nodeRecord = new NodeRecord(nodeModel.getUuid(), nodeModel.getFinalIpAddress(), true, keyHolder.getPublicKey());
        nodeAdressingSystemModel.postNodeRecord("/node-service/register-nodes", nodeRecord);
        List<NodeRecord> allNodes = nodeAdressingSystemModel.getAllNodes("/node-service/get-nodes");
        assert allNodes != null;
        List<NodeRecord> activeNodes = allNodes.stream().filter(NodeRecord::isActive).collect(Collectors.toList());

        if (activeNodes.size() == 1 && blockRepository.getBlockAllBlock().isEmpty()) {
            blockRepository.persistBlock(new BlockEntity(1, "000", new LinkedList<TransactionEntity>(), nodeModel.getUuid(), keyHolder.getPrivateKey()));
        } else {
            replicateChain(activeNodes);
        }
    }


    /**
     * This method is called when the application is closed performs clean up operations including:
     * 1) Register the node to node recording system that node's status as false
     */
    @PreDestroy
    public void finilize() {
        // First register to node recording System
        NodeRecord nodeRecord = new NodeRecord(nodeModel.getUuid(), nodeModel.getFinalIpAddress(), false, keyHolder.getPublicKey());
        nodeAdressingSystemModel.postNodeRecord("/node-service/register-nodes", nodeRecord);

    }


    /**
     * This method is used to retrieve the longest blockchain in the chain
     * @return List of blocks in the chain
     */
    public List<BlockEntity> retrieveAllBlock() {
        return blockRepository.getBlockAllBlock();
    }

    public BlockEntity mineBlock() {

        var lastBlock = blockRepository.getBlockLastBlock();
        BlockEntity blockEntity = getBlockEntity(lastBlock);

        List<NodeRecord> activeNodes = getActiveNodes();

        byte[] senderPublicKey = nodeAdressingSystemModel.getPublicKeyByUUID(blockEntity.getSender_uuid());

        if (blockChainModel.isValid(lastBlock, blockEntity, senderPublicKey)) {
            List<TransactionEntity> transactionList = blockEntity.getTransactionList();
            transactionRepository.saveAll(transactionList);
            blockRepository.persistBlock(blockEntity);


            activeNodes.forEach(nodeRecord -> {
                blockChainModel.postBlock(nodeRecord.getIpAddress(), blockEntity);
            });
        }


        return blockEntity;
    }

    private List<NodeRecord> getActiveNodes(){
        List<NodeRecord> allNodes = nodeAdressingSystemModel.getAllNodes("/node-service/get-nodes");
        assert allNodes != null;

        return allNodes.stream().filter(NodeRecord::isActive).toList();
    }

    /**
     * This method is used to retrieve the last block in the chain also used for mining a new block
     * @return Last block in the chain
     */
    private BlockEntity getBlockEntity(BlockEntity lastBlock) {
        int newIndex = blockRepository.getLastIndex() + 1;

        List<TransactionEntity> pendingTransactions;
        try {
            pendingTransactions = transactionHolder.getPendingTransactions();
            pendingTransactions.forEach(transactionEntity -> {
                transactionEntity.setMainBlock(lastBlock);
            });
        } catch (Exception e) {
            pendingTransactions = new ArrayList<>();
        }
        return new BlockEntity(newIndex, lastBlock.getCurrentBlockHash(), pendingTransactions, nodeModel.getUuid(), keyHolder.getPrivateKey());
    }

    /**
     * This method is used to retrieve the block with the given index
     * @param index Index of the block
     * @return Block with the given index
     */
    public BlockEntity retrieveBlockByIndex(int index) {
        var block = blockRepository.getBlockByIndex(index);
        return block;

    }

    /**
     * This method is used to replicate the longest blockchain to current node
     * @param nodeRecords Hash of the block
     */
    private void replicateChain(List<NodeRecord> nodeRecords) {
        // Compare all nodes and determine the longest chain
        NodeRecord majorNode = null;
        int selfLength = blockRepository.getBlockAllBlock().size();

        for (NodeRecord nodeRecord : nodeRecords) {
            RestTemplate restTemplate1 = new RestTemplate();
            if (nodeRecord.getIpAddress().equals(nodeModel.getFinalIpAddress())) {
                continue;
            }
            String urlS1 = nodeRecord.getIpAddress() + "/block";
            ResponseEntity<GenericListResponseDTO<BlockEntity>> responseEntity = restTemplate1.exchange(
                    urlS1,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GenericListResponseDTO<BlockEntity>>() {}
            );

            GenericListResponseDTO<BlockEntity> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.getData().size() > selfLength) {
                majorNode = nodeRecord;
                selfLength = responseBody.getData().size();
            }
        }

        if (majorNode == null) {
            return;
        }

        // Delete all blocks in the local database
        blockRepository.deleteAllBlocks();

        // Copy blockchain from the major node to own application
        String url = majorNode.getIpAddress() + "/block";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GenericListResponseDTO<BlockEntity>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GenericListResponseDTO<BlockEntity>>() {}
        );
        GenericListResponseDTO<BlockEntity> responseBody = response.getBody();
        if (responseBody != null) {
            List<BlockEntity> blockEntities = responseBody.getData();
            blockEntities.forEach(blockRepository::persistBlock);
        }
    }

    /**
     * This method is used to validate all blocks in the chain
     * @return true if all blocks are valid, false if there is an invalid block
     */
    public boolean validateAllBlock() {
        List<BlockEntity> allBlocks = blockRepository.getBlockAllBlock();

        for (int i = 0; i < allBlocks.size() - 1; i++) {
            var previousBlock = allBlocks.get(i);
            var currentBlock = allBlocks.get(i + 1);
            byte[] senderPublicKey = nodeAdressingSystemModel.getPublicKeyByUUID(currentBlock.getSender_uuid());
            if (!blockChainModel.isValid(previousBlock, currentBlock, senderPublicKey)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets block entity from external nodes and adds itself chain if block is valid
     *
     * @param recentBlock a recently posted block entity to add to blockChain
     * @return true : if block is  valid , false if block is invalid
     */
    public boolean acceptExternalBlock(BlockEntity recentBlock) {
        var lastNode = blockRepository.getBlockLastBlock();

        byte[] senderPublicKey = nodeAdressingSystemModel.getPublicKeyByUUID(recentBlock.getSender_uuid());

        if (blockChainModel.isValid(lastNode, recentBlock, senderPublicKey)) {
            return blockRepository.persistBlock(recentBlock);
        }
        return false;
    }

    @Scheduled(fixedRate = 10000)
    public void roundRobinConsensus() {
        List<NodeRecord> activeNodes = getActiveNodes();

        /*
        System.out.println(nodeModel.isMiner());

        for (NodeRecord activeNode : activeNodes){
            System.out.println(activeNode.getIpAddress());
        }
         */

        boolean success = false;

        if (nodeModel.isMiner()) {
            mineBlock();
            nodeModel.setMiner(false);

            for (int i = 0; i < activeNodes.size(); i++) {
                if (activeNodes.get(i).getUuid().equals(nodeModel.getUuid())) {
                    int nextIndex = (i + 1) % activeNodes.size();

                    int startIndex = nextIndex;

                    do {
                        String nextNodeIpAddress = activeNodes.get(nextIndex).getIpAddress();
                        success = blockChainModel.postMiningQualification(nextNodeIpAddress);
                        if (success) {
                            break;
                        } else {
                            System.err.println("Failed to notify the next node (" + nextNodeIpAddress + ") about mining qualification.");
                            nextIndex = (nextIndex + 1) % activeNodes.size();
                        }
                    } while (nextIndex != startIndex);

                    if (success) {
                        break;
                    }
                }
            }
        }

        if (!success) {
            nodeModel.setMiner(true);
        }
    }

    public void changeMinerStatus(){
        nodeModel.setMiner(true);
    }

}
