package com.example.blockchain.DataLayer.Repositories.Interfaces;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockChainPagingSortingRepository extends JpaRepository<BlockEntity, Integer>, PagingAndSortingRepository<BlockEntity,Integer> {
}
