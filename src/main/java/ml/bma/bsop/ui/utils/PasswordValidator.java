/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

/**
 *
 * @author ironman
 */
public class PasswordValidator implements Validator<String>{
    

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if(value.length() < 8) {
            return new ValidationResult() {
                @Override
                public String getErrorMessage() {
                    return "usererror.password.short";
                }

                @Override
                public boolean isError() {
                    return true;
                }
                
            };
        }
        return ValidationResult.ok();
    }
    
    
    
}
