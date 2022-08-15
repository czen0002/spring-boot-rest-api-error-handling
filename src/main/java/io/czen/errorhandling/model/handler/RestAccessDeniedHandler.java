package io.czen.errorhandling.model.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.czen.errorhandling.model.error.ErrorBuilder;
import io.czen.errorhandling.model.error.Errors;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Errors errors = new Errors().errorList(Collections.singletonList(ErrorBuilder.buildForbiddenError(accessDeniedException.getMessage())));
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(responseStream, errors);
    }
}
