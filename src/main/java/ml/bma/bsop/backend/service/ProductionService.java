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
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.ui.utils.CzechComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */
@Service
public class ProductionService extends CrudService<Production> {

    private static final List<String> NAME_LIST = new ArrayList<>();
    
    private final ProductionRepository productionRepository;
    private final CompositionRepository compositionRepository;
    
    private final CzechComparator comparator;
    
    @Autowired
    public ProductionService(ProductionRepository productionRepository, CompositionRepository compositionRepository) {
        this.productionRepository = productionRepository;
        this.comparator = new CzechComparator();
        this.compositionRepository = compositionRepository;
        NAME_LIST.addAll(productionRepository.findAllNames());
        Collections.sort(NAME_LIST, comparator);
    }

    @Override
    protected ProductionRepository getRepository() {
        return this.productionRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if(filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return getRepository().count();
        }
    }

    @Override
    public List<Production> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByNameLikeIgnoreCase(repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }
    
    @Override
    public Production save(Production production) {        
        if(!production.isEmpty()) {
            Production old = getRepository().findOne(production.getId());
            if(!old.getName().equalsIgnoreCase(production.getName())) {
                NAME_LIST.remove(old.getName());
                NAME_LIST.add(production.getName());
                Collections.sort(NAME_LIST, comparator);   
            }
        } else {
            NAME_LIST.add(production.getName());
            Collections.sort(NAME_LIST, comparator);   
        }           
        return super.save(production);
    }
    
    @Override
    public void delete(Production production) {
        if(NAME_LIST.contains(production.getName())) {
            NAME_LIST.remove(production.getName());
        }
        getRepository().delete(production);
    }
    
    public Production findProduction(String name) {
        return getRepository().findOneByName(name);
    }
    
    public List<String> getNameList() {
        return NAME_LIST;
    }

    @Override
    public List<Production> findAll() {
        return getRepository().findAll();
    }
    
    public List<String> getCompositionsNameList() {
        return this.compositionRepository.findAllNames();
    }
    
    public List<Composition> getCompositionsList() {
        return this.compositionRepository.findAll();
    }
}
