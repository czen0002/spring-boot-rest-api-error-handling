package io.czen.errorhandling;

import io.czen.errorhandling.model.error.Errors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntegrationTest {

    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    @Autowired
    private TestRestTemplate template;

    @Test
    public void expectUnauthorized_WhenPasswordIsWrong() throws Exception {
        String url = "/v1/student?studentId=10";
        ResponseEntity<Errors> response = template.withBasicAuth("apiuser", "wrong-password")
                        .getForEntity(url, Errors.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals(INVALID_CREDENTIALS, response.getBody().getErrorList().get(0).getTitle());
        Assertions.assertEquals(String.valueOf(SC_UNAUTHORIZED), response.getBody().getErrorList().get(0).getCode());
    }

    @Test
    public void expectForbidden_WhenApiUserCallPostStudent() throws Exception {

    }
}
