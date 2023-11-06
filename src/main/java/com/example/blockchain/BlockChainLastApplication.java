package com.example.blockchain;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockChainLastApplication implements CommandLineRunner{

    @Autowired
    BlockRepository blockRepository;





    public static void main(String[] args) {
        SpringApplication.run(BlockChainLastApplication.class, args);


    }

    public void run(String... args) throws Exception{
        System.out.println("hello");


    }
}
