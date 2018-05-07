/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import ml.bma.bsop.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ironman
 */

@SpringComponent
@UIScope
public class EmailExistValidator implements Validator<String> {    
    
    
    private final AccountService accountService;
    
    private boolean mustExist=false;
    private boolean resend = false;
    
    @Autowired
    public EmailExistValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    

    @Override
    public ValidationResult apply(String email, ValueContext context) {
        
        if(!email.isEmpty() && email.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
            if(accountService.existsEmail(email.toLowerCase())) {
                if(mustExist) {
                    return ValidationResult.ok();
                }
                if(accountService.findOneByEmail(email).isEnable()) {
                    return setErrorResult("usererror.email.exists");
                }
                this.resend = true;
                return ValidationResult.ok();
            } else {
                if(mustExist) {
                    return setErrorResult("usererror.email.notexist");
                }
                return ValidationResult.ok();
            }
        }
        return setErrorResult("usererror.email.badvalue");
    }
    
    
    private ValidationResult setErrorResult(String errMessageI18N) {
        
        return new ValidationResult() {
            @Override
            public String getErrorMessage() {
                return errMessageI18N;                
            }

            @Override
            public boolean isError() {
                return true;
            }

        };
    }
    
    public void setMustExist(boolean mustExist){
        this.mustExist = mustExist;
    }

    public boolean isResending() {
        return this.resend;
    }
    
}
