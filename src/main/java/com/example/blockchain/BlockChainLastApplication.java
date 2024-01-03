package com.example.blockchain;

import com.example.blockchain.DataLayer.Repositories.Interfaces.ReviewRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is responsible for running the Main Spring application.
 */
@SpringBootApplication
public class BlockChainLastApplication implements CommandLineRunner {

    private final ReviewRequestRepository reviewRequestRepository;

    public BlockChainLastApplication(ReviewRequestRepository reviewRequestRepository) {
        this.reviewRequestRepository = reviewRequestRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlockChainLastApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
