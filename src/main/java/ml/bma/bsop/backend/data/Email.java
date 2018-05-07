/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import java.io.Serializable;
import ml.bma.bsop.backend.data.entity.Token;

/**
 *
 * @author ironman
 */
public interface Email extends Serializable {
    
    public String getMessage();
    
    public String getSubject();
    
}
