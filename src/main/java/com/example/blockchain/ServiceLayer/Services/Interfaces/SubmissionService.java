package com.example.blockchain.ServiceLayer.Services.Interfaces;

import com.example.blockchain.DataLayer.Entities.ArticleEmbeddable;
import com.example.blockchain.DataLayer.Entities.SubmitEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * This interface is used to define the methods that will be used in the SubmissionServiceImplementation class.
 * Primary purpose of this interface is to define abstract set of methods that will used
 * To perform businesses logic operations on the Submissions that will  in the transaction .
 */
public interface SubmissionService {

    /**
     * This method is used to create a new Submission Transaction in latest block.
     * @return void This returns nothing.
     */
    void submitArticle(ArticleEmbeddable articleEmbeddable) ;
}
