package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Block")
public class BlockEntity {

    @Id
    @Column(name = "index_no")
    private int indexNo;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "nonce")
    private int nonce;

    @Column(name = "previous_block_hash")
    private String previousBlockHash;

    @Column(name = "current_block_hash")
    private String currentBlockHash;

    @Column(name = "merkle_root")
    private String merkleRoot;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "block_id")
    List<TransactionEntity> transactionList;

    public BlockEntity(int index, String previousHash){
        this.indexNo = index;
        this.nonce = 0;
        this.previousBlockHash = previousHash;
        this.transactionList = new ArrayList<>();
        this.timestamp = new Date().toString();
        this.merkleRoot = calculateMerkleRoot();
    }

    public BlockEntity(int index, String previousHash, List<TransactionEntity> transactions) {
        this.indexNo = index;
        this.nonce = 0;
        this.previousBlockHash = previousHash;
        this.transactionList = transactions;
        this.timestamp = new Date().toString();;
        this.merkleRoot = calculateMerkleRoot();
        this.currentBlockHash = this.ProofOfWork();
    }

    public BlockEntity() {
        nonce = 0;
        previousBlockHash  = timestamp = merkleRoot = null;
        transactionList = new ArrayList<>();
    }

    public String ProofOfWork() {
        String target = generateTarget();

        String input;
        String hash;

        do {
            nonce++;
            input = indexNo + timestamp + nonce + previousBlockHash + transactionList.toString();
            hash = calculateHash(input);
        } while (!hash.startsWith(target));

        return hash;
    }

    public String calculateHash(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert byte array to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String calculateMerkleRoot(){
        List<String> transactionHashes = new ArrayList<>();

        if(transactionHashes.size() == 0){
            return "000000000000";
        }else{
            for (TransactionEntity transaction : transactionList) {
                transactionHashes.add(transaction.calculateTransactionHash());
            }

            while (transactionHashes.size() > 1) {
                List<String> newHashes = new ArrayList<>();

                for (int i = 0; i < transactionHashes.size(); i += 2) {
                    String combinedHash = transactionHashes.get(i);
                    if (i + 1 < transactionHashes.size()) {
                        combinedHash += transactionHashes.get(i + 1);
                    }

                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashBytes = digest.digest(combinedHash.getBytes());

                        StringBuilder hexString = new StringBuilder();
                        for (byte hashByte : hashBytes) {
                            String hex = Integer.toHexString(0xff & hashByte);
                            if (hex.length() == 1)
                                hexString.append('0');
                            hexString.append(hex);
                        }
                        newHashes.add(hexString.toString());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                transactionHashes = newHashes;
            }

            return transactionHashes.get(0);
        }

    }

    public static String generateTarget() {
        int difficulty = 4;

        StringBuilder targetBuilder = new StringBuilder();
        for (int i = 0; i < difficulty; i++) {
            targetBuilder.append('0');
        }
        return targetBuilder.toString();
    }
}