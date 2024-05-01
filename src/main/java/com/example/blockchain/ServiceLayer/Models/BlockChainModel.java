package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
     * @return true if blockchain valid, false if not
     */
    public boolean isValid(BlockEntity previousBlock, BlockEntity currentBlock){
        try{
            String target = BlockEntity.generateTarget();

            MessageDigest md = MessageDigest.getInstance("SHA-256");

                if(!currentBlock.getPreviousBlockHash().equals(previousBlock.getCurrentBlockHash())){

                    return false;
                }

                var current_block_hash = currentBlock.getCurrentBlockHash();

                if (!current_block_hash.startsWith(target)) {
                    return false;
                }
                // TODO Transaction imzasi kontrol edilmeli

                return true;


        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
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
