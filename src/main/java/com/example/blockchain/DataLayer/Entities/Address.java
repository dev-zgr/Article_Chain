package com.example.blockchain.DataLayer.Entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * This embeddable class represents the address information associated with an article.
 * It includes details such as country, state, and zip code.
 * It is meant to be embedded within Article Embeddable to store address details.
 */
@Data
@Embeddable
@Table(name = "transaction")
public class Address {

    /**
     * Fields for storing the country.
     */
    @NotBlank(message = "Country can't be blank")
    private String country;

    /**
     * Fields for storing the state.
     */

    @NotBlank(message = "State can't be blank")
    private String state;

    /**
     * Fields for storing the zip code.
     */
    @NotBlank(message = "zipCode can't be blank")
    private String zipCode;

    /**
     * Constructor for creating an instance of Address with all attributes.
     *
     * @param country The country which the author(s) is/are in.
     * @param state   The state which the author(s) is/are in.
     * @param zipCode The zip code of the country the author(s) is/are in.
     */
    public Address(String country, String state, String zipCode) {
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    /**
     * Default constructor for creating an instance of Address with default values.
     */
    public Address() {
        this.country = state = zipCode = "";
    }
}
