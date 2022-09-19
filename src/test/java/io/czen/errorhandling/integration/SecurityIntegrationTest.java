package io.czen.errorhandling.integration;

import io.czen.errorhandling.model.Student;
import io.czen.errorhandling.model.error.Error;
import io.czen.errorhandling.model.error.Errors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

import java.io.IOException;

import static io.czen.errorhandling.testutil.Util.readFileAsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIntegrationTest {

    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    private static final String FORBIDDEN = "FORBIDDEN";
    private static final String BAD_REQUEST = "BAD_REQUEST";

    @Autowired
    private TestRestTemplate template;

    @Nested
    @DisplayName("get student")
    class getStudent {
        @Test
        public void expectAccept_WhenApiUserCallGetStudent() {
            String url = "/v1/student?studentId=10";
            ResponseEntity<Student> response = template.withBasicAuth("apiuser", "password")
                    .getForEntity(url, Student.class);
            Student responseBody = response.getBody();
            Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            Assertions.assertEquals(10, responseBody.getId());
            Assertions.assertEquals("Wayne", responseBody.getFirstName());
            Assertions.assertEquals("Bruce", responseBody.getLastName());
        }

        @Test
        public void expectUnauthorized_WhenPasswordIsWrong() {
            String url = "/v1/student?studentId=10";
            ResponseEntity<Errors> response = template.withBasicAuth("apiuser", "wrong-password")
                    .getForEntity(url, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            Assertions.assertEquals(INVALID_CREDENTIALS, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED.value()), responseError.getCode());
        }

        @Test
        public void expectBadRequest_WhenThrowMethodArgumentTypeMismatchException() {
            String url = "/v1/student?studentId=1.5";
            ResponseEntity<Errors> response = template.withBasicAuth("apiuser", "password")
                    .getForEntity(url, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertEquals(BAD_REQUEST, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), responseError.getCode());
        }

        @Test
        public void expectBadRequest_WhenThrowConstraintViolationException() {
            String url = "/v1/student?studentId=0";
            ResponseEntity<Errors> response = template.withBasicAuth("apiuser", "password")
                    .getForEntity(url, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertEquals(BAD_REQUEST, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), responseError.getCode());
        }
    }

    @Nested
    @DisplayName("post student")
    class postStudent {
        @Test
        public void expectCreated_WhenAdminUserCallPostStudent() throws IOException {
            String url = "/v1/student";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(readFileAsString("student.json"), headers);
            ResponseEntity<Student> response = template.withBasicAuth("admin", "password")
                    .postForEntity(url, request, Student.class);
            Student responseStudent = response.getBody();
            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertEquals(123, responseStudent.getId());
            Assertions.assertEquals("Tony", responseStudent.getFirstName());
            Assertions.assertEquals("Parker", responseStudent.getLastName());
        }

        @Test
        public void expectUnauthorized_WhenPasswordIsWrong() throws IOException {
            String url = "/v1/student";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(readFileAsString("student.json"), headers);
            ResponseEntity<Errors> response = template.withBasicAuth("admin", "wrong-password")
                    .postForEntity(url, request, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            Assertions.assertEquals(INVALID_CREDENTIALS, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED.value()), responseError.getCode());
        }

        @Test
        public void expectForbidden_WhenApiUserCallPostStudent() throws Exception {
            String url = "/v1/student";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(readFileAsString("student.json"), headers);
            ResponseEntity<Errors> response = template.withBasicAuth("apiuser", "password")
                    .postForEntity(url, request, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            Assertions.assertEquals(FORBIDDEN, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.FORBIDDEN.value()), responseError.getCode());
        }

        @Test
        public void expectBadRequest_WhenStudentIdNotProvided() throws Exception {
            String url = "/v1/student";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(readFileAsString("invalid_student.json"), headers);
            ResponseEntity<Errors> response = template.withBasicAuth("admin", "password")
                    .postForEntity(url, request, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertEquals(BAD_REQUEST, responseError.getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), responseError.getCode());
        }

        @Test
        public void expectBadRequest_WhenInvalidJsonPayLoad() throws Exception {
            String url = "/v1/student";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(readFileAsString("invalid_json_payload.json"), headers);
            ResponseEntity<Errors> response = template.withBasicAuth("admin", "password")
                    .postForEntity(url, request, Errors.class);
            Error responseError = response.getBody().getErrorList().get(0);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertEquals(BAD_REQUEST, response.getBody().getErrorList().get(0).getTitle());
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), responseError.getCode());
            Assertions.assertEquals("Invalid JSON request payload", responseError.getDetail());
        }
    }
}
