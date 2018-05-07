/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long>{
    
    public static final String FIND_NAMES = "SELECT name FROM production";

    public long countByNameLikeIgnoreCase(String repositoryFilter);

    public List<Production> findByNameLikeIgnoreCase(String repositoryFilter);
    
    @Query(value = FIND_NAMES, nativeQuery = true)
    public List<String> findAllNames();
    
    public Production findOneByName(String name);
    
}
