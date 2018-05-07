/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.server.StreamResource.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author ironman
 */
public class MyPdfSource implements StreamSource {
    
    private final byte[] buffer;
    
    public MyPdfSource(byte[] pdfBuffer) {
        this.buffer = pdfBuffer;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(this.buffer);
    }
    
}
