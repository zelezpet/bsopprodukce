/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.CompositionRepository;
import ml.bma.bsop.backend.ProductionRepository;
import ml.bma.bsop.backend.data.entity.Composition;
import ml.bma.bsop.ui.utils.CzechComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */

@Service
public class CompositionService extends CrudService<Composition> {
    
    private static final List<String> NAME_LIST = new ArrayList<>();

    private final CompositionRepository compositionRepository;
    private final ProductionRepository productionRepository;
    
    private final CzechComparator comparator;
    
    @Autowired
    public CompositionService(CompositionRepository compositionRepository, 
            ProductionRepository productionRepository) {
        
        this.compositionRepository = compositionRepository;
        this.productionRepository = productionRepository;
        this.comparator = new CzechComparator();
        NAME_LIST.addAll(compositionRepository.findAllNames());
        Collections.sort(NAME_LIST, comparator);
    }

    @Override
    protected CompositionRepository getRepository() {
        return this.compositionRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().countByNameLikeIgnoreCaseOrAuthorLikeIgnoreCase(repositoryFilter, repositoryFilter);
        } else {
                return getRepository().count();
        }
    }

    @Override
    public List<Composition> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByNameLikeIgnoreCaseOrAuthorLikeIgnoreCase(repositoryFilter, repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }
    
    @Override
    public Composition save(Composition composition) {
        NAME_LIST.add(composition.getName());
        Collections.sort(NAME_LIST, comparator);        
        return super.save(composition);
    }
    
    public List<String> getNameList() {
        return NAME_LIST;
    }

    @Override
    public List<Composition> findAll() {
        return getRepository().findAll();
    }

    public List<Composition> findByProductionAndFilter(String productionName, Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            if(productionName != null && !productionName.isEmpty()) {
                return getRepository().findMatchProductionNameAndFilter(productionName, repositoryFilter);
            } else {
                return getRepository().findByNameLikeIgnoreCaseOrAuthorLikeIgnoreCase(repositoryFilter, repositoryFilter);
            }
        } else {
            if(productionName != null && !productionName.isEmpty()) {
                return this.productionRepository.findOneByName(productionName).getCompositions();
            } else {
                return findAll();
            }
        }
    }
    
    
    
}
