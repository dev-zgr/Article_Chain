package com.example.blockchain.DataLayer.Entities;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tx_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @Column(name = "tx_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long tx_id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "p_hash")
    private String p_hash;

    @ManyToOne
    BlockEntity mainBlock;

    public TransactionEntity(String p_hash) {
        this.p_hash = p_hash;
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.timestamp = LocalDateTime.parse(now.format(formatter));
    }

    public TransactionEntity() {
        this.tx_id = 0;
        this.timestamp = null;
        this.p_hash = null;
    }

    public String calculateTransactionHash() {
        String transactionData = String.valueOf(tx_id) + timestamp;

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