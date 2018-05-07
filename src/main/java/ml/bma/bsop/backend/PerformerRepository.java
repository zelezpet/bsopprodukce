/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {
    
    public Performer findOneByAccount(Account account);

    public List<Performer> findByFirstnameLikeIgnoreCaseOrSurnameLikeIgnoreCase(String firstNameLike, String surnameLike);
    
}
