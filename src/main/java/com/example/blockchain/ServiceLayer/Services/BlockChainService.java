package com.example.blockchain.ServiceLayer.Services;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.NodeRecord;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlockChainService {
    private final BlockRepository blockRepository;
    private final TransactionRepository transactionRepository;

    private final BlockChainModel blockChainModel;



    @Autowired
    public BlockChainService(BlockRepository blockRepository, TransactionRepository transactionRepository, BlockChainModel blockChainModel) {
        this.blockRepository = blockRepository;
        this.transactionRepository = transactionRepository;
        this.blockChainModel = blockChainModel;

    }





    @PostConstruct
    public void init() {
        //first register to node recording System

        RestTemplate registeringTemplate = new RestTemplate();
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UUID uuid = blockChainModel.getUuid();
        String ipAdress = "http://localhost:8000";
        NodeRecord nodeRecord = new NodeRecord(uuid, ipAdress, true);

        try{
            registeringTemplate.postForObject("http://localhost:8081/node-service/register-nodes" ,nodeRecord , NodeRecord.class);

        }catch (Exception e){
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange(
                "http://localhost:8081/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {}
        );

        var element = response.getBody();
        var activeNodes = element.stream().filter(s -> s.isActive() == true).collect(Collectors.toList());


        if(activeNodes.size() == 1){
            // if this node is the only node that is active
            blockRepository.persistBlock(new BlockEntity(1, new Date().toString(), 0, "000", new TransactionEntity()));

        }else{
            replicateChain(activeNodes);
        }


    }


    @PreDestroy
    public void finilize(){
        //first register to node recording System

        RestTemplate registeringTemplate = new RestTemplate();
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UUID uuid = blockChainModel.getUuid();
        String ipAdress = "http://localhost:8000";
        NodeRecord nodeRecord = new NodeRecord(uuid, ipAdress, false);

        try{
            registeringTemplate.postForObject("http://localhost:8081/node-service/register-nodes" ,nodeRecord , NodeRecord.class);

        }catch (Exception e){
        }
    }




    public List<BlockEntity> retrieveAllBlock(){
        return blockRepository.getBlockAllBlock();
    }

    public BlockEntity mineBlock() {

        var lastBlock = blockRepository.getBlockLastBlock();
        int lastBlockNonce = lastBlock.getNonce();

        int newNonce = BlockChainModel.findNonce(lastBlockNonce);
        int newIndex = blockRepository.getLastIndex() + 1;

        BlockEntity blockEntity = new BlockEntity(newIndex ,newNonce,lastBlock.getCurrentBlockHash());
        blockChainModel.setTransactionEntities(new ArrayList<TransactionEntity>());
        blockEntity.setCurrentBlockHash(blockEntity.calculateHash());

        blockRepository.persistBlock(blockEntity);


        return blockEntity;
    }

    private void replicateChain(List<NodeRecord> nodeRecords){
        //if there is more block than our blocks in the chain set that node as a major node

        // compare all nodes and determine the longest chain
        NodeRecord nodeRecord = nodeRecords.stream()
                .max((s1,s2) -> {
                    RestTemplate restTemplate1 = new RestTemplate();
                    String urlS1 = s1.getIpAdress() + "/block-chain/get_chain";
                    String urlS2 = s2.getIpAdress() + "/block-chain/get_chain";

                    ResponseEntity<List<BlockEntity>> response1 = restTemplate1.exchange(
                            urlS1, HttpMethod.GET, null, new ParameterizedTypeReference<List<BlockEntity>>() {}
                    );

                    ResponseEntity<List<BlockEntity>> response2 = restTemplate1.exchange(
                            urlS2, HttpMethod.GET, null, new ParameterizedTypeReference<List<BlockEntity>>() {}
                    );

                    var element1 = response1.getBody();
                    var element2 = response2.getBody();

                    if(element1.size() > element2.size()){
                        return 1;
                    }else if(element1.size() < element2.size()){
                        return -1;
                    }else {
                        return 0;
                    }
                })
                .get();

        if(nodeRecord.getUuid().equals(this.blockChainModel.getUuid())){
            return;
        }

        //delete all blocks in db
        blockRepository.deleteAllBlocks();

        // copy blockchain to own application
        String url = nodeRecord.getIpAdress() +  "/blockchain/get_chain";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<BlockEntity>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BlockEntity>>() {}
        );
        List<BlockEntity> blockEntities = response.getBody();
        assert blockEntities != null;
        blockEntities.stream()
                .map(s -> blockRepository.persistBlock(s));

    }

}
