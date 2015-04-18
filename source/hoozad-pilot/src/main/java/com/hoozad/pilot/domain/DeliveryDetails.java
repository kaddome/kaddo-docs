package com.hoozad.pilot.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class DeliveryDetails implements Serializable{

    @Field("address_line_1")
    private String addressLine1;
    @Field("address_line_2")
    private String addressLine2;
    private String city;
    private String postcode;
    private String country;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
            "addressLine1='" + addressLine1 + '\'' +
            ", addressLine2='" + addressLine2 + '\'' +
            ", city='" + city + '\'' +
            ", postcode='" + postcode + '\'' +
            ", country='" + country + '\'' +
            '}';
    }
}
