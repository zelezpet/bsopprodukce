/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.AbilityRepository;
import ml.bma.bsop.backend.CastingRepository;
import ml.bma.bsop.backend.PerformanceRepository;
import ml.bma.bsop.backend.data.CastState;
import ml.bma.bsop.backend.data.Instrument;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */

@Service
public class CastingService extends CrudService<Casting> {
    
    private final CastingRepository castingRepository;
    private final PerformanceRepository performanceRepository;
    private final AbilityRepository abilityRepository;
    
    @Autowired
    public CastingService(CastingRepository castingRepository, PerformanceRepository performanceRepository, AbilityRepository abilityRepository) {
        this.castingRepository = castingRepository;
        this.performanceRepository = performanceRepository;
        this.abilityRepository = abilityRepository;
    }    

    @Override
    protected CastingRepository getRepository() {
        return this.castingRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return (getRepository().findAnyMatchByFilter(repositoryFilter, Instrument.getInstrumentLike(filter.get()), CastState.getCastStateLike(filter.get()))).size();
        } else {
            return getRepository().count();
        }
    }

    @Override
    public List<Casting> findAnyMatching(Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findAnyMatchByFilter(repositoryFilter, Instrument.getInstrumentLike(filter.get()), CastState.getCastStateLike(filter.get()));
        } else {
            return getRepository().findAll();
        }
    }

    @Override
    public List<Casting> findAll() {
        return this.castingRepository.findAll();
    }
    
    public List<Performance> findAllPerformances() {
        return this.performanceRepository.findAll();
    }

    public List<Ability> findAllAbilities() {
        return this.abilityRepository.findAll();
    }

    public List<Casting> findByPerformanceAndAnyMatching(Performance performance, Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByPerformanceAndAnyMatching(performance, repositoryFilter, Instrument.getInstrumentLike(filter.get()), CastState.getCastStateLike(filter.get()));
        } else {
            return getRepository().findByPerformance(performance);
        }
    }
    
    public List<Casting> findAllInvited(Performance performance) {
        return getRepository().findAllByCastStateAndPerformance(CastState.INVITED, performance);
    }
    
    public List<Casting> findAllLogged(Performance performance) {
        return getRepository().findAllByCastStateAndPerformance(CastState.LOGGED, performance);
    }
    
    public List<Casting> findAllConfirmed(Performance performance) {
        return getRepository().findAllByCastStateAndPerformance(CastState.CONFIRMED, performance);
    }
    
    public boolean existCasting(Ability ability) {
        List<Casting> castings = getRepository().findAllByAbility(ability);
        return !castings.isEmpty();
    }
    
    public boolean existCastingLogged(Ability ability) {
        Casting casting = getRepository().findOneByAbilityAndCastState(ability, CastState.LOGGED);
        return casting != null;
    }

    public Casting findByAbility(Ability ability) {
        List<Casting> castings = getRepository().findAllByAbility(ability);
        return castings.isEmpty()? null :castings.get(0);
    }
    
    public void savePerformance(Performance performance) {
        this.performanceRepository.save(performance);
    }
    
    public List<Casting> findByPerformance(Performance performance) {
        return getRepository().findByPerformance(performance);
    }
    
    @Override
    public Casting save(Casting casting) {
        Performance performance = casting.getPerformance();
        if(performance.getPhase() == PerformanceState.CREATE) {
            performance.setPhase(PerformanceState.CAST);
            savePerformance(performance);
        }
        return super.save(casting);
    }
    
    @Override
    public void delete(Casting casting) {
        super.delete(casting);
        Performance performance = casting.getPerformance();
        List<Casting> castings = findByPerformance(performance);
        if(castings.isEmpty()) {
            performance.setPhase(PerformanceState.CREATE);
            savePerformance(performance);
        }        
    }
}
