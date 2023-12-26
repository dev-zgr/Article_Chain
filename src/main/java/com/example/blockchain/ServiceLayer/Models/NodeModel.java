package com.example.blockchain.ServiceLayer.Models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Data
@Component
public class NodeModel {
    @Value("${node.address:localhost}")
    private String nodeAddress;

    @Value("${server.port:8080}")
    private String nodePort;

    @Value("${node.uuid:00000000-0000-0000-0000-000000000000}")
    private UUID uuid;

    public NodeModel(String nodeAddress, String nodePort, UUID uuid) {
        this.nodeAddress = nodeAddress;
        this.nodePort = nodePort;
        this.uuid = uuid;
    }

    public NodeModel(){
        nodeAddress = "localhost";
        nodePort = "8080";
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }


    public String getFinalIpAdress(){
        return "https://"+nodeAddress;
    }
}
