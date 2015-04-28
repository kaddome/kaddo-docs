package com.hoozad.pilot.web.rest.dto;

import com.hoozad.pilot.domain.DeliveryDetails;
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

    private String langKey;
    private DeliveryDetails deliveryDetails;

    private List<String> roles;

    private boolean openProfile;

    private Set<ExternalAccount> externalAccounts = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(String login, String firstName, String lastName, String langKey,
                   DeliveryDetails deliveryDetails, List<String> roles) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.langKey = langKey;
        this.roles = roles;
        this.deliveryDetails = deliveryDetails;
    }

    public UserDTO(String firstName, String lastName, ExternalAccount externalAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Set<ExternalAccount> getExternalAccounts() {
        return Collections.unmodifiableSet(externalAccounts);
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public boolean isOpenProfile() {
        return openProfile;
    }

    public void setOpenProfile(boolean openProfile) {
        this.openProfile = openProfile;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", langKey='" + langKey + '\'' +
        ", roles=" + roles +
        ", externalAccounts=" + externalAccounts +
        '}';
    }
}
