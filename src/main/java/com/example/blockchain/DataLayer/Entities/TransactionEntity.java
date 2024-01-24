package com.example.blockchain.DataLayer.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base entity representing a transaction in the blockchain.
 * It provides common attributes and methods for all types of transactions.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SubmitEntity.class, name = "submitEntity"),
})
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tx_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "transaction")
public class TransactionEntity {

    /**
     * Fields for storing the ID of the transaction.
     */
    @Id
    @Column(name = "tx_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tx_id;

    /**
     * Fields for storing the timestamp of the transaction.
     */
    @Column(name = "timestamp")
    private String timestamp;

    /**
     * Fields for storing the hash of the transaction. This fields creates Many-To-One relationship with the BlockEntity.
     */
    @ManyToOne
    @JsonBackReference
    BlockEntity mainBlock;

    /**
     * Default constructor for creating an instance of TransactionEntity with default values.
     */
    public TransactionEntity() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = now.format(formatter);
    }

    /**
     * Calculates the hash of the transaction using its ID and timestamp.
     *
     * @return The SHA-256 hash of the transaction.
     */
    public String calculateTransactionHash() {
        String transactionData = tx_id + timestamp;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(transactionData.getBytes());

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