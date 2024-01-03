package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

/**
 * This class represents a record for a node in the blockchain.
 * Each node is identified by a unique UUID and includes information about its IP address and activity status.
 */
@Data
public class NodeRecord {

    private UUID uuid;


    private String ipAdress;

    boolean isActive;

    /**
     * Constructor for creating an instance of NodeRecord with a specified UUID, IP address, and activity status.
     *
     * @param uuid      The UUID of the node.
     * @param ipAdress The IP address of the node.
     * @param isActive  The activity status of the node.
     */
    public NodeRecord(UUID uuid, String ipAdress , boolean isActive){
        this.ipAdress = ipAdress;
        this.uuid = uuid;
        this.isActive = isActive;
    }

    /**
     * Default constructor for creating an instance of NodeRecord with default values.
     */
    public NodeRecord(){
        this.uuid = null;
        this.ipAdress =null;
        this.isActive = false;
    }
}

