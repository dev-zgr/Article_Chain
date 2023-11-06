package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TransactionRepository {
    //create operations
    public boolean persistTransaction(Transaction transaction);

    //read operations
    public Transaction getTransactionById(int id);
    public List<Transaction> getTransactionByBlock(BlockEntity blockEntity);

    // delete operations
    boolean deleteTransaction(Transaction transaction);
    boolean deleteTransactionById(int index);

}
