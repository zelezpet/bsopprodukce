/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.repository;

import ml.bma.bsop.backend.AccountRepository;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.Authority;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author ironman
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {
    
    @Autowired
    private TestEntityManager entityMan;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Test
    public void whenFindByEmail_thenReturnAccount() {
        
        Account me = new Account("frantaflinta@seznam.cz", "",Authority.ROLE_USER);
        String pass = "password";      
        me.setPassword(pass);
        entityMan.persist(me);
        entityMan.flush();
                
        Account found = accountRepo.findOneByEmail(me.getEmail());
        System.out.println("");
        System.out.println(found.getPassword() + " matched " + pass);
        System.err.println(new BCryptPasswordEncoder().matches(pass, found.getPassword()));
        System.out.println("");
        assertThat(found.getEmail()).isEqualTo(me.getEmail());
        
    }
    
}
