package com.example.blockchain.ServiceLayer.Models;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class BlockEnvelope {
    private BlockEntity blockEntity;
    private UUID uuid;

    public BlockEnvelope(BlockEntity blockEntity, UUID uuid){
        this.blockEntity = blockEntity;
        this.uuid = uuid;
    }
}
