package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class is responsible for managing the blockchain model
 * It handles block adding logic and block validation logic
 */
@Component
@Data
public class BlockChainModel {
    /**
     * UUID of the individual node
     */
    private UUID uuid;

    /**
     * List of transactions in the blockchain
     */
    private List<TransactionEntity> transactionEntities;

    /**
     * Maximum number of transactions per block
     */
    @Value("${blockchain.maxTransactionsPerBlock:10}")
    private int maxTransactionsPerBlock;


    /**
     * Default constructor
     */
    public BlockChainModel(){
        uuid = new UUID(0,0);
        transactionEntities = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     * @param uuid UUID of the individual node
     * @param transactionEntities List of transactions in the blockchain
     * @param maxTransactionsPerBlock Maximum number of transactions per block
     */
    public BlockChainModel(UUID uuid, List<TransactionEntity> transactionEntities, int maxTransactionsPerBlock) {
        this.uuid = uuid;
        this.transactionEntities = transactionEntities;
        this.maxTransactionsPerBlock = maxTransactionsPerBlock;
    }

    /**
     * calculates the hash of given bytes
     * @param hash bytes to be hashed
     * @return hash of the bytes
     */
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Checks if blockchain valid based on previous and current block by comparing hashes
     * and recalculating the hash of the current block
     * @param previousBlock previous block in the blockchain
     * @param currentBlock current block in the blockchain
     * @param senderPublicKey public key of the current block's creator
     * @return true if blockchain valid, false if not
     */
    public boolean isValid(BlockEntity previousBlock, BlockEntity currentBlock, byte[] senderPublicKey){
        try{
            String target = BlockEntity.generateTarget(4);

            if(!currentBlock.getPreviousBlockHash().equals(previousBlock.getCurrentBlockHash())){
                return false;
            }

            var current_block_hash = currentBlock.getCurrentBlockHash();

            if (!current_block_hash.startsWith(target)) {
                return false;
            }

            byte[] digitalSignature = currentBlock.getDigital_signature();

            PublicKey rawPublicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(senderPublicKey));

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(rawPublicKey);
            signature.update(current_block_hash.getBytes());

            if (!signature.verify(digitalSignature)){
                return false;
            }

            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean isValidTransaction(){
        //TODO This method should be completed.
        return true;
    }

    /**
     * Posts recent mined block to external nodes
     *
     * @param ipAddress   ip address of external node
     * @param blockToPost block to post
     */
    public void postBlock(String ipAddress, BlockEntity blockToPost){
        RestTemplate restTemplate = new RestTemplate();

        try{
            var response = restTemplate.postForObject(ipAddress +"/block_chain/block", blockToPost, String.class);
            if(response != null){
            }
        }catch (Exception ignored){
        }

    }


 }
