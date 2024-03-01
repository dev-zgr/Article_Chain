package com.example.blockchain.PresentationLayer.DataTransferObjects;

import com.example.blockchain.DataLayer.Entities.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthorDTO {
    @NotEmpty(message = "Title is required")
    @Size(max = 10, message = "Title must be up to 10 characters")
    private String title;

    /**
     * Fields for storing the name of the author(s) of the article.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String author_name;
    /**
     * Fields for storing the email address of the author(s). It's must be a valid email address.
     */
    @Email
    private String author_email;
    /**
     * Fields for storing the institution to which the author(s) is/are affiliated.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "institution must be up to 100 characters")
    private String institution;

    /**
     * Fields for storing the department within the institution to which the author(s) belongs.
     */
    @NotEmpty(message = "Type is required")
    @Size(max = 100, message = "department must be up to 100 characters")
    private String department;

    private Address address;

}
