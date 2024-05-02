package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.util.*;

/**
 * This class is responsible for storing the waiting transactions
 * inside a json file.
 * This file maintains pending transactions as like a queue in a JSON file
 */
@Component
@Data
public class TransactionHolder {

    @Value("${block.maxTransactions:10}")
    private int transactionSize;

    /**
     * This is the queue that'll be used to store the transactions
     */
    private LinkedList<TransactionEntity> waitingTransactions;

    /**
     * This is the file that'll be used to store the transactions
     */
    private File resourceFile;
    ObjectWriter objectWriter;


    /**
     * This constructor initializes the object writer and the file that'll be used to store the transactions
     */
    public TransactionHolder(){
        String rootPath = System.getProperty("user.dir");

        this.resourceFile = new File(rootPath + "\\src\\main\\resources\\waiting-transactions.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example.blockchain.DataLayer.Entities")
                .allowIfSubType("java.util.LinkedList")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);


        objectWriter = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try{
            waitingTransactions = objectMapper.readValue(resourceFile, LinkedList.class);
        }catch (IOException e){
            waitingTransactions = new LinkedList<>();
            e.printStackTrace();
        }
    }

    /**
     * This method takes the pending transactions and adds it to tail of queue in it a JSON file
     * @param pendingTransaction Pending transaction that'll be added to block and mined
     * @throws JsonProcessingException json processing error may occur during serializing
     * @throws IOException IO error  may occur during serializing
     * @Return true if the transaction is added to queue successfully
     */
    public boolean addPendingTransaction(TransactionEntity pendingTransaction) throws JsonProcessingException, IOException {
        boolean add = waitingTransactions.add(pendingTransaction);
        objectWriter.writeValue(resourceFile, waitingTransactions);
        return add;
    }

    /**
     *
     * @return List of transactions that'll be processed. If there is less than specified transaction
     * it'll return all of them, if there is more than specified transaction it'll return specified number of
     * transactions and remove them from queue
     * @throws JsonProcessingException occurs when there is an error during serializing
     * @throws IOException occurs when there is an error during acessing file
     */
    public List<TransactionEntity> getPendingTransactions() throws JsonProcessingException, IOException{
        List<TransactionEntity> transactionsToProcess = new LinkedList<>();

        if( waitingTransactions.size() <  transactionSize){
            transactionsToProcess.addAll(waitingTransactions);
            waitingTransactions.clear();
            objectWriter.writeValue(resourceFile, waitingTransactions);
        }else{
          for(int i = 0; i< transactionSize; i++){
              transactionsToProcess.add((TransactionEntity) waitingTransactions.peek());
              waitingTransactions.poll();
          }
            objectWriter.writeValue(resourceFile, waitingTransactions);

        }
        return transactionsToProcess;
    }
}


