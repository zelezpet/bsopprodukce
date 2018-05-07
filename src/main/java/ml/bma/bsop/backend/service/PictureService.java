/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import ml.bma.bsop.backend.PictureRepository;
import ml.bma.bsop.backend.data.entity.Picture;
import ml.bma.bsop.ui.BSOPProductionUI;
import ml.bma.bsop.ui.utils.CzechComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */

@Service
public class PictureService extends CrudService<Picture> {
    
    
    
    private static final List<String> NAME_LIST = new ArrayList<>();

    private final PictureRepository pictureRepository;
    private final CzechComparator comparator;
    
    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
        this.comparator = new CzechComparator();
        NAME_LIST.addAll(pictureRepository.findAllNames());
        Collections.sort(NAME_LIST, comparator);
        setDefault();
    }
    
    public List<String> getNameList() {
        return NAME_LIST;
    }
    
    
    @Override
    protected PictureRepository getRepository() {
        return this.pictureRepository;
    }

    

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().countByNameLikeIgnoreCase(repositoryFilter);
        } else {
                return getRepository().count();
        }
    }

    @Override
    public List<Picture> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByNameLikeIgnoreCase(repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }
    
    public Image getImage(String name) {
        byte[] buffer = pictureRepository.findStreamByName(name);
        StreamResource imageResource = new StreamResource(() -> new ByteArrayInputStream(buffer), name);
        imageResource.setCacheTime(0);
        Image image = new Image(name, imageResource);
        image.markAsDirty();
        return image;
    }
    
    public boolean setImage(ClassPathResource resource) {
        try {
            InputStream is = resource.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[5000000];
            
            while((nRead = is.read(data, 0, data.length)) != -1) {
                os.write(data, 0, nRead);
            }
            
            os.flush();
            Picture defaultPic = new Picture(Picture.DEFAULT, os.toByteArray());
            save(defaultPic);
            
        } catch (IOException ex) {
            Logger.getLogger(BSOPProductionUI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @Override
    public Picture save(Picture picture) {
        if(NAME_LIST.contains(picture.getName())) {
            return null;
        }
        NAME_LIST.add(picture.getName());
        Collections.sort(NAME_LIST, comparator);
        super.save(picture);
        return picture;
    }
    
    public final boolean setDefault() {
        boolean exist = getRepository().countByNameLikeIgnoreCase(Picture.DEFAULT) > 0;
        if(!exist) {
            return setImage(new ClassPathResource(Picture.DEFAULT_PATH));
        }
        return true;
    }

    @Override
    public List<Picture> findAll() {
        return getRepository().findAll();
    }
    
    public Picture getPicture(String name) {
        return getRepository().findOneByName(name);
    }
    
    
}
