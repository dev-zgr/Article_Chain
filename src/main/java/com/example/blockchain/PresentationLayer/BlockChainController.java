package com.example.blockchain.PresentationLayer;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.ServiceLayer.Services.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Block;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block_chain")
public class BlockChainController {

    BlockChainService blockChainService;

    @Autowired
    public BlockChainController(BlockChainService blockChainService){
        this.blockChainService = blockChainService;
    }

    @GetMapping("/block")
    public ResponseEntity<List<BlockEntity>> getAllBlocks(){
        var block = blockChainService.retrieveAllBlock();

        if(block == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<List<BlockEntity>>(block, HttpStatus.OK);
        }

    }

    @GetMapping("/block/{id}")
    public ResponseEntity<BlockEntity> getBlockByIndex(@PathVariable int id){
        var  block = blockChainService.retrieveBlockByIndex(id);

        if(block == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(block,HttpStatus.OK);
        }

    }


    /**
     *
     * @param blockEntity that coming from other nodes
     * @return s a information string about the posted block
     */
    @PostMapping(value = "/block" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getExternalBlock(@RequestBody BlockEntity blockEntity){

        if(blockEntity == null){
            return new ResponseEntity<>("Request Body Missing!", HttpStatus.BAD_REQUEST);
        }else{
            if(blockChainService.acceptExternalBlock(blockEntity)){
                return new ResponseEntity<>("Request Accepted!", HttpStatus.BAD_GATEWAY);
            }else{
                return new ResponseEntity<>("Request Couldn't Processed", HttpStatus.BAD_GATEWAY);
            }
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
