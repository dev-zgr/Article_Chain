package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.NodeRecord;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This class used to represent Node addressing system in NRS system.
 */
@Data
@Component
public class NodeAdressingSystemModel {

    /**
     * This field used to represent the current node's address.
     */
    @Value("${node.addressingSystem.ip:localhost}")
    private String nodeAddress;

    /**
     * This field used to represent the current node's port.
     */
    @Value("${node.addressingSystem.port:8080}")
    private String nodePort;

    /**
     * This constructor used to create a NodeAdressingSystemModel with the nodeAddress and nodePort.
     * @param nodeAddress current node's address
     * @param nodePort current node's port
     */
    public NodeAdressingSystemModel(String nodeAddress, String nodePort) {
        this.nodeAddress = nodeAddress;
        this.nodePort = nodePort;
    }

    /**
     * This constructor used to create a NodeAddressingSystemModel with the default nodeAddress and nodePort.
     * Constructor used when no nodeAddress and nodePort provided. Its construct class with localhost and 8080.
     */
    public NodeAdressingSystemModel() {
        nodeAddress = "localhost";
        nodePort = "8080";
    }

    /**
     * This method used to get the current node's address. by combining the nodeAddress and nodePort and HTTP protocol.
     * @return String This returns the current node's address.
     */
    public String getAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(nodeAddress);
        stringBuilder.append(":");
        stringBuilder.append(nodePort);
        return stringBuilder.toString();
    }

    /**
     * This method post a node record to the node addressing directory using HTTP protocol.
     * @param directory directory that node record will be posted to
     * @param nodeRecord node record that will be posted
     */
    public void postNodeRecord(String directory, NodeRecord nodeRecord) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(getAddress() + directory, nodeRecord, NodeRecord.class);

        } catch (Exception e) {
        }
    }

    /**
     * This method get all node records from the node addressing directory using HTTP GET.
     * @param directory directory that node records will be get from
     * @return List<NodeRecord> This returns all node records in the directory.
     */
    public List<NodeRecord> getAllNodes(String directory) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<NodeRecord>> response = restTemplate.exchange(  getAddress()+"/node-service/get-nodes", HttpMethod.GET, null, new ParameterizedTypeReference<List<NodeRecord>>() {
        });
        return response.getBody();
    }
}
