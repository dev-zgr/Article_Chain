package com.example.blockchain.DataLayer.Entities;

import lombok.Data;

import java.util.UUID;

/**
 * This class represents a record for a node in the blockchain.
 * Each node is identified by a unique UUID and includes information about its IP address and activity status.
 */
@Data
public class NodeRecord {

    /**
     * Fields for storing the UUID of the node.
     */
    private UUID uuid;

    /**
     * Fields for storing the IP address of the node.
     */
    private String ipAddress;

    /**
     * Fields for storing the activity status of the node. True if active, false otherwise.
     */
    boolean isActive;

    /**
     * Fields for storing the public key of the node.
     */
    private byte[] publicKey;

    /**
     * Constructor for creating an instance of NodeRecord with a specified UUID, IP address, and activity status.
     *
     * @param uuid      The UUID of the node.
     * @param ipAddress The IP address of the node.
     * @param isActive  The activity status of the node.
     * @param publicKey The public key of the node
     */
    public NodeRecord(UUID uuid, String ipAddress, boolean isActive, byte[] publicKey){
        this.ipAddress = ipAddress;
        this.uuid = uuid;
        this.isActive = isActive;
        this.publicKey = publicKey;
    }

    /**
     * Default constructor for creating an instance of NodeRecord with default values.
     */
    public NodeRecord(){
        this.uuid = null;
        this.ipAddress = null;
        this.isActive = false;
        this.publicKey = null;
    }
}

