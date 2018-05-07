/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import ml.bma.bsop.backend.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    public Token findOneByToken(String token);
    
}
