package com.example.blockchain.ServiceLayer.Models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Data
@Component
public class KeyHolder {
    private byte[] publicKey;

    private PrivateKey privateKey;

    public KeyHolder(){
        generateKeyPair("12345678");

        this.publicKey = null;
        this.privateKey = null;
    }

    public boolean generateKeyPair(String password){
        boolean success = true;

        return success;
    }

    public void loadKeys(String password, String alias, String keyFileName) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyFileName), password.toCharArray());
            this.privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

            Certificate certificate = keyStore.getCertificate(alias);
            PublicKey rawPublicKey = certificate.getPublicKey();
            this.publicKey = rawPublicKey.getEncoded();

            //System.out.println("Keys loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
