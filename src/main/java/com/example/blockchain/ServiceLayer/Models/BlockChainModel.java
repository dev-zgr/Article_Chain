package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Data
public class BlockChainModel {
    private UUID uuid;
    private List<TransactionEntity> transactionEntities;

    public BlockChainModel(){
        uuid = new UUID(0,0);
        transactionEntities = new ArrayList<>();
    }


    public static int  findNonce(int previousNonce) {
        int newNonce = 1;
        boolean checkNonce = false;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            while (!checkNonce) {
                String hashOps = bytesToHex(md.digest(String.valueOf(newNonce * newNonce - previousNonce * previousNonce).getBytes()));

                if (hashOps.startsWith("0000")) {
                    checkNonce = true;
                } else {
                    newNonce++;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return newNonce;
    }

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

    public boolean isValid(BlockEntity previousBlock, BlockEntity currentBlock){
        try{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
                if(!currentBlock.getPreviousBlockHash().equals(previousBlock.getCurrentBlockHash())){

                    return false;
                }

                var previousNonce = previousBlock.getNonce();
                var currentNonce = currentBlock.getNonce();

                String hashOps = bytesToHex(md.digest(String.valueOf(currentNonce * currentNonce - previousNonce * previousNonce).getBytes()));

                if (!hashOps.startsWith("0000")) {
                    return false;
                }

                return true;


        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Posts recent mined block to external nodes
     * @param ipAddress ip address of external node
     * @param blockToPost block to post
     * @return true if block posted successfully false otherwise
     */
    public boolean postBlock(String ipAddress, BlockEntity blockToPost){
        RestTemplate restTemplate = new RestTemplate();

        try{
            var response = restTemplate.postForObject(ipAddress +"/block_chain/block", blockToPost, String.class);
            if(response != null){
                return response.equals("Request Accepted!");
            }
            return false;
        }catch (Exception e){
            return false;
        }

    }


 }
