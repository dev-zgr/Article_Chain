package com.example.blockchain.PresentationLayer.DataTransferObjects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserAccountDTO {
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", message = "Password must be 8-16 characters long and include at least one letter and one digit.")
    private String password;

    public UserAccountDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
