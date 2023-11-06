package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
public class NodeRecord {

    private UUID uuid;


    private String ipAdress;

    boolean isActive;

    public NodeRecord(UUID uuid, String ipAdress , boolean isActive){
        this.ipAdress = ipAdress;
        this.uuid = uuid;
        this.isActive = isActive;
    }

    public NodeRecord(){
        this.uuid = null;
        this.ipAdress =null;
        this.isActive = false;
    }
}

