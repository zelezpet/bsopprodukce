/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.Country;

/**
 *
 * @author ironman
 */
@Entity
public class CellPhone extends AbstractEntity {
    
    @Column(length = 10)
    @NotNull
    private String number;
    
    @Column(length = 3)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country country;

    public CellPhone() {
        //default
    }

    public CellPhone(String number, Country country) {
        this.number = number;
        this.country = country;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash + Integer.parseInt(number.substring(0,5));
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
        final CellPhone other = (CellPhone) obj;
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        if (this.country != other.country) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.country.getPreset() + this.number;
    }
    
    
}
