package com.example.blockchain.DataLayer.Entities;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "author")
    private String author;


    @Column(name = "date")
    private String date;

    @Column(name = "article")
    private String article;

    @ManyToOne
    BlockEntity mainBlock;

    public TransactionEntity(String author, String date, String article) {
        this.author = author;
        this.date = date;
        this.article = article;
    }

    public TransactionEntity() {
        this.id = 0;
        this.article = this.author = this.date =null;
    }

    public String calculateTransactionHash() {
        String transactionData = author + date + article;

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