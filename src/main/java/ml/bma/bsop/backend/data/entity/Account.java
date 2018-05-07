/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.Authority;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ironman
 */

@Entity
public class Account extends AbstractEntity {
    
    @Column(unique=true)
    @NotNull(message = "E-mail is required")
    @Email(message = "Bad e-mail format")
    private String email;
    
    @Column
    @NotNull(message = "Password is required")
    private String password;
    
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority authority;
    
    @Column
    @NotNull
    private boolean locked = false;
    
    @Column
    @NotNull
    private boolean enable = false;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Token token;

    public Account() {
        this("","",Authority.ROLE_USER);
    }
    
    public Account(String email, Authority authority) {
        this(email,null,authority);
    }
    
    public Account(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }    
    
    public void setByAccount(Account newAccount) {
        this.email = newAccount.getEmail();
        this.authority = newAccount.getAuthority();
        this.enable = newAccount.isEnable();
        this.locked = newAccount.isLocked();
        this.token = newAccount.getToken();
        this.password = newAccount.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String rawPassword) {
        this.password = new BCryptPasswordEncoder().encode(rawPassword);
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }    

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public String getRole() {
        return this.authority.name();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
    
}
