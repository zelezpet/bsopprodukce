/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    
    public static final String FIND_NAMES = "SELECT name FROM picture";
    public static final String FIND_STREAM = "SELECT stream FROM picture WHERE name = ?1";
    
    public Picture findOneByName(String name);
    
    public long countByNameLikeIgnoreCase(String nameLike);

    public List<Picture> findByNameLikeIgnoreCase(String nameLike);
    
    @Query(value = FIND_NAMES, nativeQuery = true)
    public List<String> findAllNames();
    
    @Query(value = FIND_STREAM, nativeQuery = true)
    public byte[] findStreamByName(String name);
    
}
