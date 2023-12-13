package com.example.blockchain.ServiceLayer.Services;

import com.example.blockchain.DataLayer.Encryption.KeyManager;
import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.NodeRecord;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import com.example.blockchain.ServiceLayer.Models.BlockChainModel;
import com.example.blockchain.ServiceLayer.Models.BlockEnvelope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlockChainService {
    private final BlockRepository blockRepository;
    private final TransactionRepository transactionRepository;

    private final BlockChainModel blockChainModel;

    private final KeyManager keyManager;
    private Cipher encrypytor;
    private Cipher decryptor;


    @Autowired
    public BlockChainService(BlockRepository blockRepository, TransactionRepository transactionRepository, BlockChainModel blockChainModel, KeyManager keyManager) {
        this.blockRepository = blockRepository;
        this.transactionRepository = transactionRepository;
        this.blockChainModel = blockChainModel;
        this.keyManager = keyManager;

        try {
            encrypytor = Cipher.getInstance("RSA");
            encrypytor.init(Cipher.ENCRYPT_MODE, keyManager.getPrivateKey());

            decryptor = Cipher.getInstance("RSA");
            decryptor.init(Cipher.DECRYPT_MODE, keyManager.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @PostConstruct
    public void init() {
        //first register to node recording System

        RestTemplate registeringTemplate = new RestTemplate();

        UUID uuid = blockChainModel.getUuid();
        String ipAdress = "http://localhost:8080";
        NodeRecord nodeRecord = new NodeRecord(uuid, ipAdress, true, keyManager.getPublicKey().getEncoded());

        try {
            registeringTemplate.postForObject("http://localhost:8081/node-service/register-nodes", nodeRecord, NodeRecord.class);

        } catch (Exception e) {
        }

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange("http://localhost:8081/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {
        });

        var element = response.getBody();
        assert element != null;
        var activeNodes = element.stream().filter(NodeRecord::isActive).collect(Collectors.toList());


        if (activeNodes.size() == 1 && blockRepository.getBlockAllBlock().isEmpty()) {

            try {
                var genesisBlock = new BlockEntity(1, new Date().toString(), 0, "000", new TransactionEntity());
                byte[] genesisBlockHashBytes = genesisBlock.getCurrentBlockHash().getBytes(StandardCharsets.UTF_8);
                byte[] genesisBlockHashBytesEncypted = encrypytor.doFinal(genesisBlockHashBytes);
                String encodedHash = Base64.getEncoder().encodeToString(genesisBlockHashBytesEncypted);

                genesisBlock.setSignature(encodedHash);

                blockRepository.persistBlock(genesisBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            replicateChain(activeNodes);
        }


    }


    @PreDestroy
    public void finilize() {
        //first register to node recording System

        RestTemplate registeringTemplate = new RestTemplate();


        UUID uuid = blockChainModel.getUuid();
        String ipAdress = "http://localhost:8000";
        NodeRecord nodeRecord = new NodeRecord(uuid, ipAdress, false, keyManager.getPublicKey().getEncoded());

        try {
            registeringTemplate.postForObject("http://localhost:8081/node-service/register-nodes", nodeRecord, NodeRecord.class);

        } catch (Exception e) {
        }
    }


    public List<BlockEntity> retrieveAllBlock() {
        return blockRepository.getBlockAllBlock();
    }

    public BlockEntity mineBlock() {

        var lastBlock = blockRepository.getBlockLastBlock();
        int lastBlockNonce = lastBlock.getNonce();

        int newNonce = BlockChainModel.findNonce(lastBlockNonce);
        int newIndex = blockRepository.getLastIndex() + 1;

        BlockEntity blockEntity = new BlockEntity(newIndex, newNonce, lastBlock.getCurrentBlockHash());
        blockChainModel.setTransactionEntities(new ArrayList<TransactionEntity>());
        blockEntity.setCurrentBlockHash(blockEntity.calculateHash());

        try {
            byte[] genesisBlockHashBytes = blockEntity.getCurrentBlockHash().getBytes(StandardCharsets.UTF_8);
            byte[] genesisBlockHashBytesEncypted = encrypytor.doFinal(genesisBlockHashBytes);
            String encodedHash = Base64.getEncoder().encodeToString(genesisBlockHashBytesEncypted);

            blockEntity.setSignature(encodedHash);
        } catch (Exception e) {
            e.printStackTrace();
        }


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange("http://localhost:8081/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {
        });
        var element = response.getBody();
        assert element != null;
        var activeNodes = element.stream().filter(NodeRecord::isActive).filter(nodeRecord -> !nodeRecord.getIpAdress().equals("http://localhost:8080")).toList();


        if (blockChainModel.isValid(lastBlock, blockEntity)) {
            blockRepository.persistBlock(blockEntity);
            activeNodes.forEach(nodeRecord -> {
                blockChainModel.postBlock(nodeRecord.getIpAdress(), new BlockEnvelope(blockEntity, blockChainModel.getUuid()));
            });
        }


        return blockEntity;
    }

    public BlockEntity retrieveBlockByIndex(int index) {
        return blockRepository.getBlockByIndex(index);
    }

    private void replicateChain(List<NodeRecord> nodeRecords) {
        //if there is more block than our blocks in the chain set that node as a major node

        // compare all nodes and determine the longest chain

        NodeRecord majorNode = null;
        int selfLength = blockRepository.getBlockAllBlock().size();
        for (NodeRecord nodeRecord : nodeRecords) {
            RestTemplate restTemplate1 = new RestTemplate();
            if (nodeRecord.getIpAdress().equals("http://localhost:8080")) {
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

    public boolean validateAllBlocks() {

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
    public boolean acceptExternalBlock(BlockEntity recentBlock, UUID uuid) {


        var lastNode = blockRepository.getBlockLastBlock();


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange("http://localhost:8081/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {
        });

        var element = response.getBody();
        assert element != null;
        NodeRecord specifiedNode = element.stream().filter(s -> s.getUuid().equals(blockChainModel.getUuid())).collect(Collectors.toList()).get(0);


        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(specifiedNode.getPublicKey());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (blockChainModel.isValid(lastNode, recentBlock) && blockChainModel.isSignatureValid(recentBlock, publicKey)) {
            return blockRepository.persistBlock(recentBlock);
        }
        return false;
    }


}
