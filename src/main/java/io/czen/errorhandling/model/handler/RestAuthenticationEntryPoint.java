package io.czen.errorhandling.model.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.czen.errorhandling.model.error.ErrorBuilder;
import io.czen.errorhandling.model.error.Errors;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(objectMapper.writeValueAsString(new Errors().errorList(
                Collections.singletonList(ErrorBuilder.buildUnauthorizedError(authException.getMessage())))));
    }
}
