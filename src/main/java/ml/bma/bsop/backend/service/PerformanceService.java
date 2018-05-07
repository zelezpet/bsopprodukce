/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.AbilityRepository;
import ml.bma.bsop.backend.AddressRepository;
import ml.bma.bsop.backend.PerformanceRepository;
import ml.bma.bsop.backend.ProductionRepository;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Production;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */
@Service
public class PerformanceService extends CrudService<Performance>{
    
    private final PerformanceRepository performanceRepository;
    private final ProductionRepository productionRepository;
    private final AddressRepository addressRepository;
    
    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository,
            AddressRepository addressRepository, ProductionRepository productionRepository) {
        this.performanceRepository = performanceRepository;
        this.productionRepository = productionRepository;
        this.addressRepository = addressRepository;
        
    }

    @Override
    protected PerformanceRepository getRepository() {
        return this.performanceRepository;
    }    

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().countByProductionNameLikeIgnoreCase(repositoryFilter);
        } else {
                return getRepository().count();
        }
    }

    @Override
    public List<Performance> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get().toUpperCase() + "%";
            return getRepository().findByAddressNameLikeIgnoreCaseOrProductionNameLikeIgnoreCase(repositoryFilter, repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }

    @Override
    public List<Performance> findAll() {
        return getRepository().findAll();
    }
    
    
    public List<Address> findAllPerformanceAddress() {
        return this.addressRepository.findAllPerformanceAddress();
    }
    
    public List<Performance> findByProductionAndFilter(String productionName, Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            if(productionName != null && !productionName.isEmpty()) {
                return getRepository().findMatchWithProductionAddressOrPhase(productionName, repositoryFilter, PerformanceState.getPerformanceStateLike(filter.get()));
            } else {
                return getRepository().findByProductionNameLikeIgnoreCaseOrAddressNameLikeIgnoreCaseOrPhase(repositoryFilter, repositoryFilter, PerformanceState.getPerformanceStateLike(filter.get()));
            }
        } else {
            if(productionName != null && !productionName.isEmpty()) {
                return getRepository().findByProductionName(productionName);
            } else {
                return getRepository().findAll();
            }
        }
    }
    
    public List<Production> findAllProduction() {
        return this.productionRepository.findAll();
    }
    
}
