package ml.bma.bsop;


import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.server.SpringVaadinServlet;
import java.io.Serializable;
import javax.servlet.ServletException;
import ml.bma.bsop.security.SecuredViewAccessControl;
import ml.bma.bsop.security.VaadinSessionSecurityContextHolderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;



@SpringBootApplication(scanBasePackages = {"ml.bma.bsop"}, exclude = SecurityAutoConfiguration.class)
@EnableVaadin
@EnableJpaRepositories(basePackages = {"ml.bma.bsop.backend"})
@EntityScan(basePackages = {"ml.bma.bsop.backend.data.entity"})
@ComponentScan
public class BSOPProductionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BSOPProductionApplication.class, args);
    }

    @Component("vaadinServlet")
    public static class CustomServlet extends SpringVaadinServlet implements Serializable {
        
        
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            
            getService().setSystemMessagesProvider(systemMessagesInfo -> {
                CustomizedSystemMessages messages = new CustomizedSystemMessages();
                // Don't show any messages, redirect immediately to the session expired URL
                messages.setSessionExpiredNotificationEnabled(false);

                // Don't show any message, reload the page instead
                messages.setCommunicationErrorNotificationEnabled(false);
                
                return messages;
            });
        }

    }
    
    @Configuration
    public static class BSOPProductionConfiguration {

        /**
         * The password encoder to use when encrypting passwords.
        * @return 
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        SecuredViewAccessControl securedViewAccessControl() {
            return new SecuredViewAccessControl();
        }
        
        @Bean
        public MessageProvider messageProvider() {
            return new ResourceBundleMessageProvider("messages");
        }
        

    }
    
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

        private final UserDetailsService userDetailsService;

        private final PasswordEncoder passwordEncoder;

        @Autowired
        public SecurityConfiguration (UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
            this.userDetailsService = userDetailsService;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
            auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);            
        }

        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManager();
        }
        
        static {
            SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
        }
        
    }
    
    @Controller
    public static class RedirectController implements ErrorController{
        private static final String ERROR_PATH = "/error";
        private static final String ERROR_FORWARD_PATH = "redirect:/";        
        
        
        @RequestMapping(value = ERROR_PATH)
        @Override
        public String getErrorPath() {
            return ERROR_FORWARD_PATH;
        }
    }
        
}
