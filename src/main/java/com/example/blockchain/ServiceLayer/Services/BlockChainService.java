package com.example.blockchain.ServiceLayer.Services;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.NodeRecord;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        element.stream().forEach(s -> System.out.println(s));
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
        System.out.println("hello");

        blockRepository.persistBlock(blockEntity);


        return blockEntity;
    }
}
