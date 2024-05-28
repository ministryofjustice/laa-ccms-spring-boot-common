package uk.gov.laa.ccms.springboot.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * API access token authentication filter.
 */
public class ApiAuthenticationFilter extends GenericFilterBean {

    ApiAuthenticationService authenticationService;

    @Autowired
    protected ApiAuthenticationFilter(ApiAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Filter reponsible for authenticating the client the made the request. Successful authentication results in the
     * authentication details being stored in the security context for further processing, and continuation of the
     * filter chain. Unsuccessful authentication results in a 401 UNAUTHORIZED response.
     *
     * @param request the http request object
     * @param response the http response object
     * @param filterChain the current filter chain
     * @throws IOException -
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException {
        try {
            Authentication authentication = authenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpResponse.getWriter().write(ex.getMessage());
        }
    }
}