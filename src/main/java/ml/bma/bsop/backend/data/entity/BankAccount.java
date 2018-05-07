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
import ml.bma.bsop.backend.data.Bank;

/**
 *
 * @author ironman
 */
@Entity
public class BankAccount extends AbstractEntity {
    
    @Column(length = 6)
    private String pre;
    
    @Column(length = 10)
    private String accountNum;
    
    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    private Bank bank;
    
    @Column(length = 31)
    private String iban;

    public BankAccount() {
        //default
    }
    

    public BankAccount(String accountNum, Bank bank) {
        this("000000",accountNum, bank, null);
    }
    
    public BankAccount(String iban) {
        this(null,null,null,iban);
    }

    public BankAccount(String prefix,String accountNum, Bank bank, String iban) {
        this.pre = prefix;
        this.accountNum = accountNum;
        this.bank = bank;
        this.iban = iban;
    }

    public String getPre() {
        return this.pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getAccountNum() {
        return this.accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash + (accountNum == null ? Integer.parseInt(iban.substring(10,17)): Integer.parseInt(iban.substring(3,7)));
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
        final BankAccount other = (BankAccount) obj;
        if (!Objects.equals(this.pre,other.pre)) {
            return false;
        }
        if (!Objects.equals(this.accountNum, other.accountNum)) {
            return false;
        }
        if (!Objects.equals(this.iban, other.iban)) {
            return false;
        }
        if (this.bank != other.bank) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        if(this.iban != null) {
            return "IBAN " + this.iban;
        }
        return this.pre + "-" + this.accountNum + "/" + this.bank.getCode();
    }

    
}
