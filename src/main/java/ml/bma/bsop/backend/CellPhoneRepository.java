/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import ml.bma.bsop.backend.data.entity.CellPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface CellPhoneRepository extends JpaRepository<CellPhone, Long> {
    
}
