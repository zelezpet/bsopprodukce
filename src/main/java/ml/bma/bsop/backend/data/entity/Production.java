/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ironman
 */

@Entity
public class Production extends AbstractEntity {
    
    @Column(unique = true)
    @NotNull
    private String name;
    
    @Column
    @NotNull
    private String description;
    
    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL)
    private List<Performance> performances;    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    private Picture picture;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "playlist",
            joinColumns = @JoinColumn(name = "production_id"),
            inverseJoinColumns = @JoinColumn(name = "composition_id"))
    private List<Composition> compositions;

    public Production() {
    }

    public Production(String name, String description, Picture picture, List<Composition> playlist) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.compositions = playlist;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }
    
    
    public Image getImage() {
        Image image;
        if(picture != null) {
            
            StreamSource source = new StreamSource() {
                @Override
                public InputStream getStream() {
                    return new ByteArrayInputStream(picture.getStream());
                }
            };
            image = new Image(this.name, new StreamResource(source,"streamedSourceFromByteArray"));
        } else {
            image = null;
        }
        return image;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
