package com.example.blockchain.PresentationLayer.Controllers;

import com.example.blockchain.PresentationLayer.DataTransferObjects.UserAccountDTO;
import com.example.blockchain.ServiceLayer.Services.Interfaces.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }


    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<UserAccountDTO> login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        UserAccountDTO userAccountDTO = userAccountService.authorizeUser(email, password);
        try{
            if (userAccountDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(null);
            } else {
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(userAccountDTO);
            }
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);

        }
    }
}
