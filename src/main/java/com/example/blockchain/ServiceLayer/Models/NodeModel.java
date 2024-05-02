package com.example.blockchain.ServiceLayer.Models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * NodeModel used to represents node's itself information in ArticleChain Software.
 */
@Data
@Component
public class NodeModel {
    /**
     * Node's  IP address.
     */
    @Value("${node.address:localhost}")
    private String nodeAddress;

    /**
     * Node's port.
     */
    @Value("${server.port:8080}")
    private String nodePort;

    /**
     * Node's UUID.
     */
    @Value("${node.uuid:00000000-0000-0000-0000-000000000000}")
    private UUID uuid;

    /**
     * NodeModel constructor.
     * @param nodeAddress Node's IP address.
     * @param nodePort Node's port.
     * @param uuid Node's UUID.
     */
    public NodeModel(String nodeAddress, String nodePort, UUID uuid) {
        this.nodeAddress = nodeAddress;
        this.nodePort = nodePort;
        this.uuid = uuid;
    }

    /**
     * NodeModel constructor.
     */
    public NodeModel(){

    }


    /**
     * Get node's final IP address. concatenate https:// with node's IP address.
     * @return Node's final IP address.
     */
    public String getFinalIpAddress(){
        return "https://" + nodeAddress + ":" + nodePort;
    }
}
