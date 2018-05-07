package ml.bma.bsop.security;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import com.vaadin.server.VaadinSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * A custom {@link SecurityContextHolderStrategy} that stores the {@link SecurityContext} in the Vaadin Session.
 */
@Configuration
@Scope
public class VaadinSessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    @Override
    public void clearContext() {
        VaadinSession session = getSession();
        if(session != null) {
            session.setAttribute(SecurityContext.class, null);
        }
    }

    @Override
    public SecurityContext getContext() {
        VaadinSession session = getSession();
        if(session == null) return null;
        SecurityContext context = session.getAttribute(SecurityContext.class);
        if (context == null) {
            context = createEmptyContext();
            session.setAttribute(SecurityContext.class, context);
        }
        return context;
    }

    @Override
    public void setContext(SecurityContext context) {
        VaadinSession session = getSession();
        if(session != null) {
            session.setAttribute(SecurityContext.class, context);
        }
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

    private static VaadinSession getSession() {
        return VaadinSession.getCurrent();
    }
}
