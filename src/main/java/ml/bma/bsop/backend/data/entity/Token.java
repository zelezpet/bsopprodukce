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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.TokenType;

/**
 *
 * @author ironman
 */

@Entity
public class Token extends AbstractEntity {
    
    @Id
    private Long id;
    
    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TokenType type;
    
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Account account;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tmpaccount_id")
    private Account tmpAccount;
    
    
    @Column(unique = true)
    @NotNull
    private String token;

    public Token() {
        //default
    }
    
    public Token(TokenType type, Account account) {
        this(type, account, null);
        
    }
    
    public Token(TokenType type, Account account, Account tmpAccount) {
        this.type = type;
        this.account = account;
        this.tmpAccount = tmpAccount;
        this.token = generateToken(60);
    }
    
    public Account getAccount() {
        return this.account;
    }


    public String getToken() {
        return this.token;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getTmpAccount() {
        return this.tmpAccount;
    }

    public void setTmpAccount(Account tmpAccount) {
        this.tmpAccount = tmpAccount;
    }

    public TokenType getType() {
        return this.type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
    
    public static String generateToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            int typeChar = (int) Math.round(Math.random()*2.0);
            char ch;
            switch(typeChar) {
                case 0:
                    ch = (char) (48 + ((int) Math.round(Math.random()*9.0)));
                    break;
                case 1:
                    ch = (char) (65 + ((int) Math.round(Math.random()*25.0)));
                    break;
                case 2:
                    ch = (char) (97 + ((int) Math.round(Math.random()*25.0)));
                    break;
                default:
                    ch = 'a';
                    break;
            }
            token.append(ch);
        }
        return token.toString();
    }
    
}
