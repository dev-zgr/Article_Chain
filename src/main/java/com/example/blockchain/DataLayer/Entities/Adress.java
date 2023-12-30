package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
@Table(name = "transaction")
public class Adress {

    @NotBlank(message = "Country can't be blank")
    private String country;

    @NotBlank(message = "State can't be blank")
    private String state;

    @NotBlank(message = "zipCode can't be blank")
    private String zipCode;

    public Adress(String country, String state, String zipCode) {
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Adress() {
        this.country = state = zipCode = "";
    }
}
