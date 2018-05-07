/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.ui.Upload.Receiver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import ml.bma.bsop.backend.data.entity.Picture;

/**
 *
 * @author ironman
 */
public class ImageReciever implements Receiver {
        
    private final ByteArrayOutputStream output;
    private String filename;
    private String mimeType;

    public ImageReciever() {
        this.output = new ByteArrayOutputStream(5000000);
    }   

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        
        this.filename = filename;
        this.mimeType = mimeType;
        this.output.reset();
        return output;
        
    }
    
    public byte[] getByte() {
        try {
            output.flush();
        } catch (IOException ex) {            
            return null;
        }
        return output.toByteArray();
    }
    
    public Picture getPicture() {
        try {
            output.flush();
        } catch (IOException ex) {            
            
            return null;
        }        
        return new Picture(filename, mimeType, output.toByteArray());
    }
    
}
