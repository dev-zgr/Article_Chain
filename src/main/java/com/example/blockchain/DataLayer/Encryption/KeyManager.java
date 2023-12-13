package com.example.blockchain.DataLayer.Encryption;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Data
@Component
@Scope("prototype")
public class KeyManager {
    private PublicKey publicKey;
    private PrivateKey privateKey;



    public KeyManager(){

        try(FileInputStream privateKeyFile = new FileInputStream("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/public.key");
            FileInputStream publicKeyFile = new FileInputStream("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/public.key")){

            if(privateKeyFile.readAllBytes().length == 0 || publicKeyFile.readAllBytes().length == 0){
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                KeyPair keyPair = generator.generateKeyPair();

                this.publicKey = keyPair.getPublic();
                this.privateKey = keyPair.getPrivate();

                publicKeyFile.close();
                privateKeyFile.close();

                FileOutputStream publicKeyOutputStream = new FileOutputStream("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/public.key");
                FileOutputStream privateKeyOutputStream = new FileOutputStream("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/private.key");

                publicKeyOutputStream.write(publicKey.getEncoded());
                privateKeyOutputStream.write(privateKey.getEncoded());

                privateKeyOutputStream.flush();
                publicKeyOutputStream.flush();

                privateKeyOutputStream.close();
                publicKeyOutputStream.close();

            }else{


                File publicKeyFileOne = new File("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/public.key");
                File privateKeyFileOne = new File("/Users/ozgurkamali/IdeaProjects/BlockChainLast/src/main/java/com/example/blockchain/DataLayer/Encryption/private.key");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFileOne.toPath());
                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFileOne.toPath());


                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                this.publicKey = keyFactory.generatePublic(publicKeySpec);

                EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                this.privateKey = keyFactory.generatePrivate(privateKeySpec);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
