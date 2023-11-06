package com.example.blockchain.PresentationLayer;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.ServiceLayer.Services.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/block_chain")
public class BlockChainController {

    BlockChainService blockChainService;

    @Autowired
    public BlockChainController(BlockChainService blockChainService){
        this.blockChainService = blockChainService;
    }

    @GetMapping("/get_chain")
    public ResponseEntity<List<BlockEntity>> getChain(){
        var block = blockChainService.retrieveAllBlock();

        if(block == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<List<BlockEntity>>(block, HttpStatus.OK);
        }

    }

    @GetMapping("/mine_block")
    public ResponseEntity<BlockEntity> mineBlock(){
        var minedBlock = blockChainService.mineBlock();

        if(minedBlock == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<BlockEntity>(minedBlock, HttpStatus.OK);
        }
    }
}
