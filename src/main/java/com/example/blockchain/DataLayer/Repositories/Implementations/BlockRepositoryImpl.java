package com.example.blockchain.DataLayer.Repositories.Implementations;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.DataLayer.Repositories.Interfaces.BlockRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class BlockRepositoryImpl implements BlockRepository {

    private EntityManager entityManagerFactory;

    @Autowired
    public BlockRepositoryImpl(EntityManager entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public boolean persistBlock(BlockEntity blockEntity) {
        try{
            entityManagerFactory.merge(blockEntity);
            return true;
         }catch (Exception e){
        e.printStackTrace();
        return false;
        }
    }

    @Override
    public BlockEntity getBlockByIndex(int index) {
        try{
        return entityManagerFactory.find(BlockEntity.class ,index);
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public List<BlockEntity> getBlockAllBlock() {
        Query query = entityManagerFactory.createQuery("SELECT e FROM BlockEntity e", BlockEntity.class);
        return query.getResultList();
    }

    @Override
    public BlockEntity getBlockLastBlock() {
        Query query = entityManagerFactory.createQuery("SELECT COUNT(e) FROM BlockEntity e");
        Long rowCount = (Long)query.getSingleResult();
        return entityManagerFactory.find(BlockEntity.class,rowCount);
    }

    @Override
    public List<BlockEntity> getLastTwoBlock() {
        Query query = entityManagerFactory.createQuery("SELECT COUNT(e) FROM BlockEntity e");
        Long rowCount = (Long) query.getSingleResult();
        var blockOne = entityManagerFactory.find(BlockEntity.class,rowCount);
        var blockTwo = entityManagerFactory.find(BlockEntity.class,rowCount -1);

        return List.of(blockOne, blockTwo);

    }

    @Override
    public boolean removeBlock(BlockEntity blockEntity) {
        try {
            entityManagerFactory.remove(blockEntity);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean removeBlockByIndex(int index) {
        var block = entityManagerFactory.find(BlockEntity.class, index);
        try{
            entityManagerFactory.remove(block);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int getLastIndex() {
        Query query = entityManagerFactory.createQuery("SELECT COUNT(e) FROM BlockEntity e");
        return  (int) (long) query.getSingleResult();
    }

    @Override
    public void deleteAllBlocks() {
        Query query = entityManagerFactory.createQuery("DELETE FROM BlockEntity ");

    }

    @Override
    public boolean updateBlock(BlockEntity blockEntity) {
        try{
            BlockEntity block = entityManagerFactory.find(BlockEntity.class, blockEntity.getIndexNo());
            block.setCurrentBlockHash(blockEntity.getCurrentBlockHash());
            block.setPreviousBlockHash(blockEntity.getPreviousBlockHash());
            block.setTransactionList(blockEntity.getTransactionList());
            entityManagerFactory.merge(block);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
