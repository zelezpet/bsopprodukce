/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    
    //public static final String FIND_BY_PRODUCTION = "SELECT performance.id FROM performance per JOIN production pro ON per.production_id = pro.id WHERE pro.name = ?1";
    
    public static final String FIND_ANY_MATCH = 
            "SELECT pro.performances FROM Performance per " +
            "JOIN per.production pro " +
            "JOIN per.address adr " +
            "WHERE (pro.name = ?1) AND (adr.name LIKE ?2 OR per.phase LIKE ?3)";
    
    public long countByProductionNameLikeIgnoreCase(String nameLike);
    
    public List<Performance> findByAddressNameLikeIgnoreCase(String addressNameLike);
    
    public List<Performance> findByAddressNameLikeIgnoreCaseOrProductionNameLikeIgnoreCase(String addressNameLike, String productionNameLike);
    
    @Query(value = FIND_ANY_MATCH)
    public List<Performance> findMatchWithProductionAddressOrPhase(String productionName, String filterLike, PerformanceState state);

    public List<Performance> findByProductionName(String productionName);

    public List<Performance> findByProductionNameLikeIgnoreCaseOrAddressNameLikeIgnoreCaseOrPhase(String ProductionNameLike, String AddressNameLike, PerformanceState stateLike);
    
}
