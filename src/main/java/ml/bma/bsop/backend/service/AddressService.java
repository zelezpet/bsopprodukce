/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.AddressRepository;
import ml.bma.bsop.backend.data.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */

@Service
public class AddressService extends CrudService<Address> {
    
    
    private final AddressRepository addressRepository;
    
    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    

    @Override
    protected AddressRepository getRepository() {
        return this.addressRepository;
    }    

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().countByNameLikeIgnoreCaseOrStreetLikeIgnoreCaseOrCityLikeIgnoreCase(
                        repositoryFilter, repositoryFilter, repositoryFilter);
        } else {
                return getRepository().count();
        }
    }

    @Override
    public List<Address> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().findByNameLikeIgnoreCaseOrStreetLikeIgnoreCaseOrCityLikeIgnoreCase(
                        repositoryFilter, repositoryFilter, repositoryFilter);
        } else {
                return getRepository().findAll();
        }
    }

    @Override
    public List<Address> findAll() {
        return getRepository().findAll();
    }

    public List<Address> findAnyMatchingPerformanceAddress(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().findMatchPerformanceAddress(repositoryFilter, repositoryFilter);
        } else {
                return getRepository().findAllPerformanceAddress();
        }
    }
    
}
