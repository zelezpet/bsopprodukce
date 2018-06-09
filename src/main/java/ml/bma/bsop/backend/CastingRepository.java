/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.CastState;
import ml.bma.bsop.backend.data.Instrument;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface CastingRepository extends JpaRepository<Casting, Long> {
    
    public static final String MATCH_BY_FILTER = "SELECT c FROM Casting c WHERE (c.ability.performer.firstname LIKE ?1) OR " +
                                                                               "(c.ability.performer.surname LIKE ?1) OR " +
                                                                               "(c.ability.instrument = ?2) OR " +
                                                                               "(c.performance.production.name LIKE ?1) OR " +
                                                                               "(c.castState = ?3)";
    public static final String MATCH_BY_PERFORMANCE_AND_FILTER = "SELECT c FROM Casting c WHERE ((c.performance = ?1) AND " +
                                                                                               "((c.ability.performer.firstname LIKE ?2) OR " +
                                                                                                "(c.ability.performer.surname LIKE ?2) OR " +
                                                                                                "(c.ability.instrument = ?3) OR " +
                                                                                                "(c.performance.production.name LIKE ?2) OR " +
                                                                                                "(c.castState = ?4)))";
    
    public static final String FIND_BY_CASTSTATE = "SELECT c FROM Casting c WHERE c.castState = ?1";
    
    @Query(value = MATCH_BY_FILTER)
    public List<Casting> findAnyMatchByFilter(String name, Instrument instrument, CastState castState);
    
    @Query(value = MATCH_BY_PERFORMANCE_AND_FILTER)
    public List<Casting> findByPerformanceAndAnyMatching(Performance performance, String repositoryFilter, Instrument instrumentLike, CastState castStateLike);

    public List<Casting> findByPerformance(Performance performance);
    
    @Query(value = FIND_BY_CASTSTATE)
    public List<Casting> findByCastState(CastState castState);
    
    public List<Casting> findAllByAbility(Ability ability);

    public Casting findOneByAbilityAndCastState(Ability ability, CastState castState);
    
    public List<Casting> findAllByCastStateAndPerformance(CastState castState, Performance performance);
}
