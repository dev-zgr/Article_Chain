package com.example.blockchain.ServiceLayer.Configurations;

import com.example.blockchain.DataLayer.Entities.NodeRecord;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Data
@Component
public class NodeAdressingSystem {

    @Value("${node.addressingSystem.ip:localhost}")
    private String nodeAddress;

    @Value("${node.addressingSystem.port:8080}")
    private String nodePort;

    public NodeAdressingSystem(String nodeAddress, String nodePort) {
        this.nodeAddress = nodeAddress;
        this.nodePort = nodePort;
    }

    public NodeAdressingSystem() {
    }

    public String getAdress() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(nodeAddress);
        stringBuilder.append(":");
        stringBuilder.append(nodePort);
        return stringBuilder.toString();
    }

    public void postNodeRecord(String directory, NodeRecord nodeRecord) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(getAdress() + directory, nodeRecord, NodeRecord.class);

        } catch (Exception e) {
        }
    }

    public List<NodeRecord> getAllNodes(String directory) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange("http://localhost:8081/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {
        });
        return response.getBody();
    }
}
