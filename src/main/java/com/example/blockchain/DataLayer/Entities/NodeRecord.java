package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
public class NodeRecord {

    private UUID uuid;


    private String ipAdress;

    boolean isActive;

    @Lob
    @Column(name="public_key", columnDefinition="bytea")
    private byte[] publicKey;

    public NodeRecord(UUID uuid, String ipAdress , boolean isActive , byte[] publicKey){
        this.ipAdress = ipAdress;
        this.uuid = uuid;
        this.isActive = isActive;
        this.publicKey = publicKey;
    }

    public NodeRecord(){
        this.uuid = null;
        this.ipAdress =null;
        this.isActive = false;
        publicKey = new byte[2048];
    }
}

