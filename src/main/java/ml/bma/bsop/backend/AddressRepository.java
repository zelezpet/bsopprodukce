/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    
    public static final String FIND_PERFORMANCE_ADDRESS = "SELECT adr FROM Address adr WHERE (adr.personal = false)";
    public static final String FIND_MATCH_PERFORMANCE_ADDRESS = "SELECT adr FROM Address adr WHERE " +
                                                                "(adr.personal = false) AND "+
                                                                "((adr.street LIKE ?1) OR " +
                                                                "(adr.city LIKE ?2))";

    @Query(value = FIND_PERFORMANCE_ADDRESS)
    public List<Address> findAllPerformanceAddress();

    public long countByNameLikeIgnoreCaseOrStreetLikeIgnoreCaseOrCityLikeIgnoreCase(String nameLike, String streetLike, String cityLike);

    public List<Address> findByNameLikeIgnoreCaseOrStreetLikeIgnoreCaseOrCityLikeIgnoreCase(String nameLike, String streetLike, String cityLike);
    
    @Query(value = FIND_MATCH_PERFORMANCE_ADDRESS)
    public List<Address> findMatchPerformanceAddress(String streetLike, String cityLike);
    
}
