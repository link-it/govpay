package it.govpay.rs.v1.authentication.provider;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UtenzeAuthenticationProvider implements AuthenticationProvider{

    private UserDetailsService userDetailsService;
    private Logger log = LoggerWrapperFactory.getLogger(this.getClass());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = authentication.getName();
            Object password = authentication.getCredentials();
            UsernamePasswordAuthenticationToken userAuth = null;

            try {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    userAuth = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
                    userAuth.setDetails(authentication.getDetails());
            }catch(UsernameNotFoundException e){
                    this.log.debug("Utenza non riconosciuta",e);
                    throw new BadCredentialsException("Utenza non riconosciuta",e);
            }

            return userAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public UserDetailsService getUserDetailsService() {
            return this.userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
    }


}
