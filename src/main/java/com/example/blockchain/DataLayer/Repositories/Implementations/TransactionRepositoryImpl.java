package com.example.blockchain.DataLayer.Repositories.Implementations;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.TransactionRepository;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {
    @Override
    public boolean persistTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public Transaction getTransactionById(int id) {
        return null;
    }

    @Override
    public List<Transaction> getTransactionByBlock(BlockEntity blockEntity) {
        return null;
    }

    @Override
    public boolean deleteTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public boolean deleteTransactionById(int index) {
        return false;
    }
}
