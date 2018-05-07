/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.AbstractField;
import java.util.Optional;

/**
 *
 * @author ironman
 */


public class ConfirmPasswordValidator implements Validator<String> {
    
    private final AbstractField password;

    public ConfirmPasswordValidator(AbstractField password) {
        this.password = password;
    }

    
    @Override
    public ValidationResult apply(String value, ValueContext context) {
        String pass = (String) this.password.getValue();
        if(value.equals(pass)) {
            return ValidationResult.ok();
        }
        return new ValidationResult() {
            @Override
            public String getErrorMessage() {
                return "usererror.password.confirm";
            }

            @Override
            public boolean isError() {
                return true;
            }

        };
    }
    
    
    
}
