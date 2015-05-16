package com.hoozad.pilot.web.rest;

import com.hoozad.pilot.domain.SharingMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestUserDataBuilder {

    private String login;
    private String firstName;
    private String lastName;
    private String langKey;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postcode;
    private String country;
    private String userRole;
    private boolean openProfile;
    private SharingMode sharingMode;

    public static TestUserDataBuilder buildTestData() {
        return new TestUserDataBuilder();
    }

    private TestUserDataBuilder() {
    }

    public TestUserDataBuilder login(String login) {
        this.login = login;
        return this;
    }

    public TestUserDataBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TestUserDataBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TestUserDataBuilder langKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public TestUserDataBuilder addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public TestUserDataBuilder addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public TestUserDataBuilder city(String city) {
        this.city = city;
        return this;
    }

    public TestUserDataBuilder postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public TestUserDataBuilder country(String country) {
        this.country = country;
        return this;
    }

    public TestUserDataBuilder userRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public TestUserDataBuilder sharingMode(SharingMode sharingMode) {
        this.sharingMode = sharingMode;
        return this;
    }

    public String build() {
        JSONObject postRequestBody = new JSONObject();

        try {

            postRequestBody.put("login", login);
            postRequestBody.put("firstName", firstName);
            postRequestBody.put("lastName", lastName);
            postRequestBody.put("langKey", langKey);
            postRequestBody.put("deliveryDetails", createDeliveryDetails());
            postRequestBody.put("roles", createUserRoles());
            postRequestBody.put("openProfile", openProfile);
            postRequestBody.put("externalAccounts", new JSONArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postRequestBody.toString();
    }

    private JSONArray createUserRoles() {
        JSONArray roles = new JSONArray();
        roles.put(userRole);
        return roles;
    }

    private JSONObject createDeliveryDetails() throws JSONException {
        JSONObject deliveryDetails = new JSONObject();
        deliveryDetails.put("addressLine1", addressLine1);
        deliveryDetails.put("addressLine2", addressLine2);
        deliveryDetails.put("City", city);
        deliveryDetails.put("postcode", postcode);
        deliveryDetails.put("country", country);
        return deliveryDetails;
    }


}
