/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ironman
 */
@Entity
public class Performer extends AbstractEntity {
    
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "account_id", updatable = true)
    private Account account;
    
    @Column
    @NotNull
    private String firstname;
    
    @Column
    @NotNull
    private String surname;
    
    @Column(unique = true)
    private String op;
    
    @Column(unique = true)
    private String pas;
        
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "phone_id")
    private CellPhone cellPhone;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "bank_id")
    private BankAccount bank;
    
    @OneToMany(mappedBy = "performer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ability> abilities;
    
    public Performer() {
        //default
    }

    public Performer(Account account, String firstName, String surname, CellPhone cellPhone, Address address, BankAccount bank) {
        this.account = account;
        this.firstname = firstName;
        this.surname = surname;
        this.cellPhone = cellPhone;
        this.address = address;
        this.bank = bank;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CellPhone getCellPhone() {
        return this.cellPhone;
    }

    public void setCellPhone(CellPhone cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BankAccount getBank() {
        return this.bank;
    }

    public void setBank(BankAccount bank) {
        this.bank = bank;
    }

    public String getOp() {
        return this.op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getPas() {
        return this.pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash + firstname.length() + surname.length(); 
        hash = hash + (op != null ? Integer.parseInt(op.substring(7,10)) : Integer.parseInt(pas.substring(6,9)));
        hash = hash + address.hashCode() + bank.hashCode() + cellPhone.hashCode();
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
        final Performer other = (Performer) obj;
        if (!this.firstname.equals(other.firstname)) {
            return false;
        }
        if (!this.surname.equals(other.surname)) {
            return false;
        }
        if((this.op != null && other.getOp() == null) || (this.op == null && other.getOp() != null)) {
            return false;
        }
        
        if(this.op != null && other.getOp() != null) {
            if(!this.op.equals(other.getOp())) {
                return false;
            }
        }
        
        if((this.pas != null && other.getPas() == null) || (this.pas == null && other.getPas() != null)) {
            return false;
        }
        
        if(this.pas != null && other.getPas() != null) {
            if(!this.pas.equals(other.getPas())) {
                return false;
            }
        }
        if (!this.account.equals(other.account)) {
            return false;
        }
        if (!this.cellPhone.equals(other.cellPhone)) {
            return false;
        }
        if (!this.address.equals(other.address)) {
            return false;
        }
        if (!this.bank.equals(other.bank)) {
            return false;
        }
        
        List<Ability> otherAbilities = other.getAbilities();
        if(abilities == null && otherAbilities == null) {
            return true;
        }
        
        if((abilities == null && otherAbilities != null) || (abilities != null && otherAbilities == null)) {
            return false;
        }
        
        if(abilities.size() != otherAbilities.size()) {
            return false;
        }
        boolean contain = false;
        for(Ability ability: abilities) {
            contain = false;
            for(Ability otherAbility: otherAbilities) {
                if(ability.equals(otherAbility)) {
                    contain = true;
                }
            }
            if(!contain) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        return surname + " " + firstname;
    }
    
    
    
}
