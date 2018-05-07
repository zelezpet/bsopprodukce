/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import ml.bma.bsop.backend.data.Country;

/**
 *
 * @author ironman
 */

@Entity
public class Address extends AbstractEntity {
    
    @Column
    private String name;
    
    @Column
    @NotNull
    @Size(min = 2, max = 255)
    @Pattern(regexp = "[1-9\\p{Lu}][0-9\\p{L}\\.]*([ \\-\\/][\\p{L}1-9][\\p{L}0-9\\.]*)*")
    private String street;
    
    @Column(length = 11)
    @NotNull
    @Size(min = 1, max = 11)
    @Pattern(regexp = "([0-9]{1,4}[A-Za-z]?)(\\/[0-9]{1,4}[A-Za-z]?)*")
    private String houseNumber;
    
    @Column
    @NotNull
    @Size(min = 2, max = 255)
    @Pattern(regexp = "[\\p{Lu}1-9][\\p{L}0-9\\.]*([ \\-\\/][\\p{L}1-9][\\p{L}0-9\\.]*)*")
    private String city;
    
    @Column(length = 5)
    @NotNull
    @Size(min = 5, max = 5)
    @Pattern(regexp = "[0-9]{5}")
    private String zipCode;
    
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country country;
    
    @Column(nullable = false)
    private boolean personal = false;
    
    public Address() {
        //default
    }

    public Address(String street, String houseNumber, String city, String zipCode, Country country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return this.houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash + street.length() * houseNumber.length() + Integer.parseInt(zipCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (!this.street.equals(other.street)) {
            return false;
        }
        if (!this.houseNumber.equals(other.houseNumber)) {
            return false;
        }
        if (!this.zipCode.equals(other.zipCode)) {
            return false;
        }
        if (this.country != other.country) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public String allToString() {
        String tmp = this.street + " " + this.houseNumber + ", " + this.city + " " + this.zipCode;
        if(this.country != Country.CZE) {
            tmp += " " + country.getNameCZ();
        }
        return tmp;
    }
    
}
