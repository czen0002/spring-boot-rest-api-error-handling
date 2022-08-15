package io.czen.errorhandling.model.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.czen.errorhandling.model.error.ErrorBuilder;
import io.czen.errorhandling.model.error.Errors;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Errors errors = new Errors().errorList(Collections.singletonList(ErrorBuilder.buildUnauthorizedError(authException.getMessage())));
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(responseStream, errors);
    }
}
