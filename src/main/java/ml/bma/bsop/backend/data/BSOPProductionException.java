/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import org.springframework.dao.DataIntegrityViolationException;

/**
 *
 * @author ironman
 */
public class BSOPProductionException extends DataIntegrityViolationException {
    
    public final static String ACCOUNT_NOT_VALIDATED = "Account is not validated by email";

    public BSOPProductionException(String message) {
        super(message);
    }
    
}
