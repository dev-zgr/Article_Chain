package com.example.blockchain.DataLayer.Entities;

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


    @Column(name = "signature", length = 2048)
    @Lob
    private String signature;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "block_id")
    List<TransactionEntity> transactionList;

    public BlockEntity(int index, int nonce, String previousHash){
        this.indexNo = index;
        this.nonce = nonce;
        this.previousBlockHash = previousHash;
        this.transactionList = new ArrayList<>();
        this.timestamp = new Date().toString();
        this.merkleRoot = calculateMerkleRoot();
    }

    public BlockEntity(int index, String timeStamp, int nonce, String previousHash, TransactionEntity transaction) {
        this.indexNo = index;
        this.nonce = nonce;
        this.previousBlockHash = previousHash;
        this.transactionList = List.of(transaction);
        this.timestamp = new Date().toString();
        this.merkleRoot = calculateMerkleRoot();
        this.currentBlockHash = this.calculateHash();
        this.signature = signature;

    }

    public BlockEntity() {
        nonce = 0;
        previousBlockHash  = timestamp =merkleRoot = null;
        transactionList = new ArrayList<>();
    }

    public BlockEntity(int index, int nonce, String previousHash, ArrayList<TransactionEntity> transaction) {
        this.indexNo = index;
        this.nonce = nonce;
        this.previousBlockHash = previousHash;
        this.transactionList = transaction;
        this.timestamp = new Date().toString();
        this.currentBlockHash = calculateHash();
        this.merkleRoot = calculateMerkleRoot();
        this.signature = signature;
    }


    public String calculateHash(){
        String input = indexNo + timestamp + nonce + previousBlockHash + transactionList.toString();
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
}