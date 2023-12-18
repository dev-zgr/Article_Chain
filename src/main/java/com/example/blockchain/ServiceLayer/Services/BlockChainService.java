package com.example.blockchain.ServiceLayer.Services;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.NodeRecord;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import com.example.blockchain.ServiceLayer.Models.NodeAdressingSystemModel;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Models.NodeModel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.NotificationEmitter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlockChainService {
    private final BlockRepository blockRepository;
    private final TransactionRepository transactionRepository;

    private final BlockChainModel blockChainModel;

    private final NodeAdressingSystemModel nodeAdressingSystemModel;

    private final NodeModel nodeModel;


    @Autowired
    public BlockChainService(BlockRepository blockRepository, TransactionRepository transactionRepository, BlockChainModel blockChainModel, NodeAdressingSystemModel nodeAdressingSystemModel, NodeModel nodeModel) {
        this.blockRepository = blockRepository;
        this.transactionRepository = transactionRepository;
        this.blockChainModel = blockChainModel;
        this.nodeAdressingSystemModel = nodeAdressingSystemModel;
        this.nodeModel = nodeModel;

    }


    @PostConstruct
    public void init() {
        //first register to node recording System
        NodeRecord nodeRecord = new NodeRecord(nodeModel.getUuid(), nodeModel.getFinalIpAdress(), true);
        nodeAdressingSystemModel.postNodeRecord("/node-service/register-nodes", nodeRecord);
        List<NodeRecord> allNodes = nodeAdressingSystemModel.getAllNodes("/node-service/get-nodes");
        assert allNodes != null;
        List<NodeRecord> activeNodes = allNodes.stream().filter(NodeRecord::isActive).collect(Collectors.toList());


        if (activeNodes.size() == 1 && blockRepository.getBlockAllBlock().isEmpty()) {
            blockRepository.persistBlock(new BlockEntity(1, new Date().toString(), "000", new TransactionEntity()));

        } else {
            replicateChain(activeNodes);
        }
    }


    @PreDestroy
    public void finilize() {
        //first register to node recording System
        NodeRecord nodeRecord = new NodeRecord(nodeModel.getUuid(), nodeModel.getFinalIpAdress(), false);
        nodeAdressingSystemModel.postNodeRecord("/node-service/register-nodes", nodeRecord);

    }


    public List<BlockEntity> retrieveAllBlock() {
        return blockRepository.getBlockAllBlock();
    }

    public BlockEntity mineBlock() {

        var lastBlock = blockRepository.getBlockLastBlock();

        int newIndex = blockRepository.getLastIndex() + 1;

        BlockEntity blockEntity = new BlockEntity(newIndex, lastBlock.getCurrentBlockHash());
        blockChainModel.setTransactionEntities(new ArrayList<TransactionEntity>());
        blockEntity.setCurrentBlockHash(blockEntity.ProofOfWork());


        List<NodeRecord> allNodes = nodeAdressingSystemModel.getAllNodes("/node-service/get-nodes");
        assert allNodes != null;
        List<NodeRecord> activeNodes = allNodes.stream().filter(NodeRecord::isActive).collect(Collectors.toList());

        if (blockChainModel.isValid(lastBlock, blockEntity)) {
            blockRepository.persistBlock(blockEntity);
            activeNodes.forEach(nodeRecord -> {
                blockChainModel.postBlock(nodeRecord.getIpAdress(), blockEntity);
            });
        }


        return blockEntity;
    }

    public BlockEntity retrieveBlockByIndex(int index) {
        var block = blockRepository.getBlockByIndex(index);
        return block;

    }

    private void replicateChain(List<NodeRecord> nodeRecords) {
        //if there is more block than our blocks in the chain set that node as a major node

        // compare all nodes and determine the longest chain

        NodeRecord majorNode = null;
        int selfLength = blockRepository.getBlockAllBlock().size();


        for (NodeRecord nodeRecord : nodeRecords) {
            RestTemplate restTemplate1 = new RestTemplate();
            if (nodeRecord.getIpAdress().equals(nodeModel.getFinalIpAdress())) {
                continue;
            }
            String urlS1 = nodeRecord.getIpAdress() + "/block_chain/block";
            ResponseEntity<List<BlockEntity>> response1 = restTemplate1.exchange(urlS1, HttpMethod.GET, null, new ParameterizedTypeReference<List<BlockEntity>>() {
            });

            if (Objects.requireNonNull(response1.getBody()).size() > selfLength) {
                majorNode = nodeRecord;
                selfLength = response1.getBody().size();
            }
        }


        if (majorNode == null) {
            return;
        }

        //delete all blocks in db
        blockRepository.deleteAllBlocks();

        // copy blockchain to own application
        String url = majorNode.getIpAdress() + "/block_chain/block";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<BlockEntity>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BlockEntity>>() {
        });
        List<BlockEntity> blockEntities = response.getBody();
        assert blockEntities != null;
        blockEntities.forEach(blockRepository::persistBlock);

    }

    public boolean validateAllBlock() {
        List<BlockEntity> allBlocks = blockRepository.getBlockAllBlock();

        for (int i = 0; i < allBlocks.size() - 1; i++) {
            var previousBlock = allBlocks.get(i);
            var currentBlock = allBlocks.get(i + 1);
            if (!blockChainModel.isValid(previousBlock, currentBlock)) {
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

        if (blockChainModel.isValid(lastNode, recentBlock)) {
            return blockRepository.persistBlock(recentBlock);
        }
        return false;
    }

}
