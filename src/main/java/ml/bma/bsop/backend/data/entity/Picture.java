/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import java.io.ByteArrayInputStream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ironman
 */

@Entity
public class Picture extends AbstractEntity {
    
    public static final String DEFAULT = "default";
    public static final String DEFAULT_PATH = "static/produkce.jpg";
    
    @Column
    @NotNull
    private String name;
    
    @Column
    private String mimeType;
    
    @Lob
    @Column(length = 5000000)
    @NotNull
    private byte[] stream;

    public Picture() {
    }

    public Picture(String name, byte[] stream) {
        this(name, null, stream);
    }    

    public Picture(String name, String mimeType, byte[] stream) {
        this.name = name;
        this.mimeType = mimeType;
        this.stream = stream;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getStream() {
        return stream;
    }

    public void setStream(byte[] stream) {
        this.stream = stream;
    }
    
    public Image getImage() {
        StreamResource imageResource = new StreamResource(() -> new ByteArrayInputStream(this.stream), this.name);
        imageResource.setCacheTime(0);
        Image image = new Image(this.name, imageResource);
        image.markAsDirty();
        return image;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
