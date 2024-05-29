package uk.gov.laa.ccms.springboot.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    ObjectMapper objectMapper;

    @Autowired
    ApiAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int code = HttpServletResponse.SC_FORBIDDEN;
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String status = Response.Status.FORBIDDEN.getReasonPhrase();
        String message = accessDeniedException.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(code, status, message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        log.info("Request rejected for endpoint '{} {}': {}", request.getMethod(), request.getRequestURI(), message);
    }

}
