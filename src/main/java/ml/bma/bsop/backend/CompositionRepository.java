/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;


import java.util.List;
import ml.bma.bsop.backend.data.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface CompositionRepository extends JpaRepository<Composition, Long>{
    
    public static final String FIND_NAMES = "SELECT name FROM composition";
    public static final String FIND_MATCH = 
            "SELECT pro.compositions FROM Composition com " +
            "JOIN com.productions pro " +
            "WHERE ((pro.name = ?1) AND ((com.name LIKE ?2) OR (com.author LIKE ?2)))";
    
    public List<Composition> findByNameLikeIgnoreCaseOrAuthorLikeIgnoreCase(String nameLike, String authorLike);

    public long countByNameLikeIgnoreCaseOrAuthorLikeIgnoreCase(String repositoryFilter1, String repositoryFilter2);
    
    @Query(value = FIND_NAMES, nativeQuery = true)
    public List<String> findAllNames();
    
    @Query(value = FIND_MATCH )
    public List<Composition> findMatchProductionNameAndFilter(String productionName, String repositoryFilter);
    
}
