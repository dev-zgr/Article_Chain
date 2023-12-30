package com.example.blockchain.DataLayer.Entities;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;

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

    @Id
    @Column(name = "tx_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tx_id;

    @Column(name = "timestamp")
    private String timestamp;

    @ManyToOne
    @JsonBackReference
    BlockEntity mainBlock;

    public TransactionEntity() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = now.format(formatter);
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