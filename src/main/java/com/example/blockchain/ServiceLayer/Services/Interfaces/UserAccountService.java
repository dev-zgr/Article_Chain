package com.example.blockchain.ServiceLayer.Services.Interfaces;

import com.example.blockchain.PresentationLayer.DataTransferObjects.UserAccountDTO;

public interface UserAccountService {
    /**
     * This method is used to authorize the user account that is  registered to the ArticleChain Application
     * @param email of the user
     * @param password password of the user
     * @return UserAccountDTO that contains user account information
     */
    UserAccountDTO authorizeUser(String email, String password);
}
