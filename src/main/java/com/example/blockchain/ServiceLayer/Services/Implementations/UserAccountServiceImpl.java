package com.example.blockchain.ServiceLayer.Services.Implementations;

import com.example.blockchain.PresentationLayer.DataTransferObjects.UserAccountDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Value("${node.addressingSystem.ip}")
    private String nrsIpAddress;

    @Value("${node.addressingSystem.port}")
    private String nrsPort;
    @Override
    public UserAccountDTO authorizeUser(String email, String password) {

        String baseUrl = "http://" + nrsIpAddress + ":" + nrsPort;
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/user?email=" + email + "&password=" + password;
        try{
            ResponseEntity<UserAccountDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, UserAccountDTO.class);

            responseEntity.getBody();
            if (responseEntity.getStatusCode().value() == 202) {
                return responseEntity.getBody();
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }
}
