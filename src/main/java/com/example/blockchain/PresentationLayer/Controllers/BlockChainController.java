package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.DataLayer.Entities.BlockEntity;
import com.example.blockchain.PresentationLayer.DataTransferObjects.GenericListResponseDTO;
import com.example.blockchain.ServiceLayer.Models.NodeModel;
import com.example.blockchain.ServiceLayer.Services.Implementations.ArticleServiceImpl;
import com.example.blockchain.ServiceLayer.Services.Implementations.BlockChainService;
import com.example.blockchain.ServiceLayer.Services.Interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is the REST Controller for the BlockChain.
 * it handles the HTTP requests and responses for the BlockChain.
 */
@RestController
@CrossOrigin(origins = "*")
public class BlockChainController {

    /**
     * This is the BlockChainService that this controller uses to handle the HTTP requests.
     */
    BlockChainService blockChainService;
    private ArticleService articleService;

    /**
     * This constructor is used to create the BlockChainController with the BlockChainService.
     *
     * @param blockChainService BlockChainService that this controller uses to handle business logic of the HTTP requests
     */
    @Autowired
    public BlockChainController(BlockChainService blockChainService, ArticleService articleService) {
        this.blockChainService = blockChainService;
        this.articleService =  articleService;
    }

    /**
     * This method is used to get all the blocks in the BlockChain.
     * it handles the HTTP GET request for the /block endpoint and produces a JSON response.
     *
     * @return a list of all the blocks in the BlockChain with HTTP200 if the request is successful.
     * HTTP204  with null body if the request is unsuccessful.
     */
//    @GetMapping(value = "/block", produces = {"application/json"})
//    public ResponseEntity<List<BlockEntity>> getAllBlocks() {
//        var block = blockChainService.retrieveAllBlock();
//
//        if (block == null) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(block, HttpStatus.OK);
//        }
//
//    }

    @GetMapping(path = "/block", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericListResponseDTO<BlockEntity>> getAllBlock(
            @RequestParam(value = "page-no", defaultValue = "0")
            int pageNo,
            @RequestParam(value = "ascending", defaultValue = "true")
            boolean ascending) {
        try{
            GenericListResponseDTO<BlockEntity> response = new GenericListResponseDTO<>();
            response.setData(articleService.getAllBlock(pageNo, ascending));
            response.setPageNumber(articleService.getBlockPageCount());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * This method is used to find the blocks by id in the BlockChain.
     * it handles the HTTP GET request for the /block endpoint and produces a JSON response.
     *
     * @param id id of the block that is requested
     * @return specified block in the BlockChain with HTTP200 if the request is successful.
     * HTTP204  with null body if there is no block with such id.
     */
    @GetMapping(value = "/block/{id}", produces = {"application/json"})
    public ResponseEntity<BlockEntity> getBlockByIndex(@PathVariable int id) {
        var block = blockChainService.retrieveBlockByIndex(id);

        if (block == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(block, HttpStatus.OK);
        }

    }

    /**
     * This method is used for keeping blockchain updated for latest blocks in the BlockChain.
     * Once another nodes mines a new block it sends (POSTs) this new block to every other node in
     * The blockchain.
     * it handles the HTTP POST request for the /block endpoint and consumes/produces a JSON response.
     *
     * @return a Response entity with String message according the requests.
     * Request Body Missing! string with HTTP400 returned if request body is missing
     * Request Accepted! string message with HTTP201 returned if block successfully accepted
     * Request Couldn't process  string message with HTTP500 returned if there is an internal server error.
     */
    @PostMapping(value = "/block", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> getExternalBlock(@RequestBody BlockEntity blockEntity) {

        if (blockEntity == null) {
            return new ResponseEntity<>("Request Body Missing!", HttpStatus.BAD_REQUEST);
        } else {
            if (blockChainService.acceptExternalBlock(blockEntity)) {
                return new ResponseEntity<>("Request Accepted!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Request Couldn't process", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    /**
     * This method is used to mine new block in blockchain
     * @return latest mined block in the blockchain
     */
    @GetMapping(value = "/mine_block" , produces = {"application/json"})
    public ResponseEntity<BlockEntity> mineBlock() {
        var minedBlock = blockChainService.mineBlock();

        if (minedBlock == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(minedBlock, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/change_miner_status")
    public ResponseEntity<String> startMining() {
        blockChainService.changeMinerStatus();

        return ResponseEntity.ok("Mining status updated successfully");
    }
}
