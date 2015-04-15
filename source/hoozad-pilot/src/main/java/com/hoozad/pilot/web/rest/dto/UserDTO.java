package com.hoozad.pilot.web.rest.dto;

import com.hoozad.pilot.domain.ExternalAccount;

import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {

    @Pattern(regexp = "^[a-z0-9]*$")
    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String langKey;

    private List<String> roles;

    private Set<ExternalAccount> externalAccounts = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(String login, String firstName, String lastName, String email, String langKey,
                   List<String> roles) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
    }

    public UserDTO(String firstName, String lastName, String email, ExternalAccount externalAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.externalAccounts.add(externalAccount);
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Set<ExternalAccount> getExternalAccounts() {
        return Collections.unmodifiableSet(externalAccounts);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", langKey='" + langKey + '\'' +
        ", roles=" + roles +
        ", externalAccounts=" + externalAccounts +
        '}';
    }
}