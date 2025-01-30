package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonDTO {
    /**
     * User's unique id primary key
     */
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String password;
    /**
     * 0 -- not verified.<p>
     * 1 -- Verified.<p>
     * 2 -- Banned .
     */
    @NotNull
    private int accountStatus;
    /**
     * Admin<p>
     * User
     */
    private String role;

    public PersonDTO() {
    }


    // Request signUp DTO
    public PersonDTO(String firstName, String lastName, String address, String email, String phone, String password, int accountStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accountStatus = accountStatus;
    }

    // Response signUp DTO
    public PersonDTO(Long id, String firstName, String lastName, String address, String email, String phone, String password, int accountStatus) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accountStatus = accountStatus;
    }

    // Response createNewUserByAdmin
    public PersonDTO(Long id, String firstName, String lastName, String address, String email, String phone, String password, int accountStatus, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.accountStatus = accountStatus;
        this.role = role;
    }

    // Requests createNewUserByAdmin
    public PersonDTO(String firstName, String lastName, String address, String email, String phone, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // Response showUserDetails
    public PersonDTO(String firstName, String lastName, String address, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank String address) {
        this.address = address;
    }

    public @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank String email) {
        this.email = email;
    }

    public @NotBlank String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank String phone) {
        this.phone = phone;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    @NotNull
    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(@NotNull int accountStatus) {
        this.accountStatus = accountStatus;
    }
}
