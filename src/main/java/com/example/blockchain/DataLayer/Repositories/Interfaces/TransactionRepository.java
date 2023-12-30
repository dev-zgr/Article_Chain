package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * JPA Repository for handling database operations of TransactionEntity
 */
public interface TransactionRepository extends  JpaRepository<TransactionEntity, Long> {
}
