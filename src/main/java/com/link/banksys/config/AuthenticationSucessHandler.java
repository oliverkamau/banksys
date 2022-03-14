package com.link.banksys.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSucessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public AuthenticationSucessHandler() {
        super();
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl("/protected/home");
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {
        System.out.println("Successful Authentication");
        redirectStrategy.sendRedirect(request, response, "/protected/home");
    }

}
