package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This entity is used for defining the block structure of the blockchain.
 * The primary goal of this entity is to represent a block in the blockchain
 * and handle operations such as proof-of-work, hash calculations, and merkle root generation.
 */

@Entity
@Data
@Table(name = "Block")
public class BlockEntity {

    /**
     * Fields for storing the index of the block in the blockchain.
     */
    @Id
    @Column(name = "index_no")
    private int indexNo;

    /**
     * Fields for storing the timestamp of the block.
     */
    @Column(name = "timestamp")
    private String timestamp;

    /**
     * Fields for storing the nonce of the block.
     */
    @Column(name = "nonce")
    private int nonce;

    /**
     * Fields for storing the hash of the previous block in the blockchain.
     */
    @Column(name = "previous_block_hash")
    private String previousBlockHash;

    /**
     * Fields for storing the hash of the current block in the blockchain.
     */
    @Column(name = "current_block_hash")
    private String currentBlockHash;

    /**
     * Fields for storing the Merkle root hash of the block.
     */
    @Column(name = "merkle_root")
    private String merkleRoot;

    @Column(name = "sender_uuid")
    private UUID sender_uuid;

    @Lob
    @Column(name = "digital_signature", columnDefinition="BLOB")
    private byte[] digital_signature;

    /**
     * Fields for storing the list of transactions in the block.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "block_id")
    List<TransactionEntity> transactionList;

    /**
     * Constructor for creating a block with a given index and previous block hash.
     *
     * @param index         The index of the block in the blockchain.
     * @param previousHash  The hash of the previous block in the blockchain.
     */
    public BlockEntity(int index, String previousHash){
        this.indexNo = index;
        this.nonce = 0;
        this.previousBlockHash = previousHash;
        this.transactionList = new ArrayList<>();
        this.timestamp = new Date().toString();
        this.merkleRoot = calculateMerkleRoot();
        this.sender_uuid = null;
        this.digital_signature = null;
    }

    /**
     * Constructor for creating a block with a given index, previous block hash, and list of transactions.
     *
     * @param index         The index of the block in the blockchain.
     * @param previousHash  The hash of the previous block in the blockchain.
     * @param transactions  List of transactions to be included in the block.
     */
    public BlockEntity(int index, String previousHash, List<TransactionEntity> transactions, UUID sender_uuid, PrivateKey privateKey) {
        this.indexNo = index;
        this.nonce = 0;
        this.previousBlockHash = previousHash;
        this.transactionList = transactions;
        this.timestamp = new Date().toString();;
        this.merkleRoot = calculateMerkleRoot();
        this.currentBlockHash = this.ProofOfWork();
        this.sender_uuid = sender_uuid;
        this.digital_signature = this.generateSignature(privateKey);
    }

    /**
     * Default constructor for a block entity.
     */
    public BlockEntity() {
        nonce = 0;
        previousBlockHash  = timestamp = merkleRoot = null;
        sender_uuid = null;
        digital_signature = null;
        transactionList = new ArrayList<>();
    }

    /**
     * Performs the proof-of-work algorithm to find the hash that meets the target criteria.
     *
     * @return The hash of the block that satisfies the proof-of-work conditions.
     */
    public String ProofOfWork() {
        String target = generateTarget();

        String input;
        String hash;

        do {
            nonce++;
            hash = calculateHash();
        } while (!hash.startsWith(target));

        return hash;
    }

    /**
     * Calculates the SHA-256 hash of the given input string.
     *
     * @return The SHA-256 hash of the input string.
     */
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

    /**
     * Calculates the Merkle root hash of the transactions in the block.
     *
     * @return The Merkle root hash of the block's transactions.
     */
    public String calculateMerkleRoot(){
        List<String> transactionHashes = new ArrayList<>();

        for (TransactionEntity transaction : transactionList) {
            transactionHashes.add(transaction.calculateTransactionHash());
        }

        if(!transactionHashes.isEmpty()){
            while (transactionHashes.size() > 1) {
                List<String> newHashes = new ArrayList<>();

                for (int i = 0; i < transactionHashes.size(); i += 2) {
                    String combinedHash = transactionHashes.get(i);
                    if (i + 1 < transactionHashes.size()) {
                        combinedHash += transactionHashes.get(i + 1);
                    } else {
                        combinedHash += transactionHashes.get(i);
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
        }else{
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(this.transactionList.toString().getBytes());

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
    }

    /**
     * Generates a target string for proof-of-work with a specified difficulty level.
     *
     * @return The target string with leading zeros based on the difficulty level.
     */
    public static String generateTarget() {
        int difficulty = 4;

        StringBuilder targetBuilder = new StringBuilder();
        for (int i = 0; i < difficulty; i++) {
            targetBuilder.append('0');
        }
        return targetBuilder.toString();
    }

    public byte[] generateSignature(PrivateKey privateKey){
        try{
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);

            byte[] messageBytes = this.currentBlockHash.getBytes();

            signature.update(messageBytes);
            byte[] digitalSignature = signature.sign();

            return digitalSignature;
        }catch (Exception e){
            return null;
        }
    }
}