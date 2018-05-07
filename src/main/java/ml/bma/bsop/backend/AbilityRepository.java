/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.Instrument;
import ml.bma.bsop.backend.data.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface AbilityRepository extends JpaRepository<Ability, Long> {
    
    public List<Ability> findAllByInstrument(Instrument instrument);
    
    
}
