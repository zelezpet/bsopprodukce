package ml.bma.bsop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Collection;
import ml.bma.bsop.backend.data.Authority;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;

/**
 * This demonstrates how you can control access to views.
 */
@Component
@Scope
public class SecuredViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            ArrayList<GrantedAuthority> accessible = new ArrayList<>(3);
            accessible.add(new SimpleGrantedAuthority(Authority.ROLE_USER.name()));
            accessible.add(new SimpleGrantedAuthority(Authority.ROLE_PERFORMER.name()));
            accessible.add(new SimpleGrantedAuthority(Authority.ROLE_ADMIN.name()));
            switch (beanName) {
                case "performerView":
                    return authorities.contains(accessible.get(0))
                        || authorities.contains(accessible.get(1));
                case "calendarView":                    
                    return authorities.contains(accessible.get(0))
                        || authorities.contains(accessible.get(1))
                        || authorities.contains(accessible.get(2));
                case "productionView":
                case "performanceView":
                case "castingView":
                case "addressView":
                case "compositionView":
                    return authorities.contains(accessible.get(2));
                default:
                    break;
            }
            
        }
        return false;
    }
}
