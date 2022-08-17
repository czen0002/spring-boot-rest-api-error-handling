package io.czen.errorhandling.controller;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.czen.errorhandling.testutil.Util.readFileAsString;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentControllerTest {

    private static final String BAD_REQUEST = "BAD_REQUEST";
    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    private static final String FORBIDDEN = "FORBIDDEN";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeAll
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Nested
    @DisplayName("get student")
    class getStudent {
        @Test
        public void expectAccept_WhenApiUserCallGetStudent() throws Exception {
            String url = "/v1/student?studentId=10";
            mvc.perform(get(url).with(httpBasic("apiuser", "password")))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.id").value(10))
                    .andExpect(jsonPath("$.firstName").value("Wayne"))
                    .andExpect(jsonPath("$.lastName").value("Bruce"));
        }

        @Test
        public void expectUnauthorized_WhenPasswordIsWrong() throws Exception {
            String url = "/v1/student?studentId=10";
            mvc.perform(get(url).with(httpBasic("apiuser", "wrong-password")))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorList[0].title").value(INVALID_CREDENTIALS))
                    .andExpect(jsonPath("$.errorList[0].code").value(SC_UNAUTHORIZED));
        }

        @Test
        public void expectBadRequest_WhenThrowMethodArgumentTypeMismatchException() throws Exception {
            String url = "/v1/student?studentId=1.5";
            mvc.perform(get(url).with(httpBasic("apiuser", "password")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                    .andExpect(jsonPath("$.errorList[0].code").value(String.valueOf(SC_BAD_REQUEST)));
        }

        @Test
        public void expectBadRequest_WhenThrowConstraintViolationException() throws Exception {
            String url = "/v1/student?studentId=0";
            mvc.perform(get(url).with(httpBasic("apiuser", "password")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                    .andExpect(jsonPath("$.errorList[0].code").value(String.valueOf(SC_BAD_REQUEST)));
        }
    }

    @Nested
    @DisplayName("post student")
    class postStudent {
        @Test
        public void expectCreated_WhenAdminCallPostStudent() throws Exception {
            String url = "/v1/student";
            mvc.perform(post(url).with(httpBasic("admin", "password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(readFileAsString("student.json")))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(123))
                    .andExpect(jsonPath("$.firstName").value("Tony"))
                    .andExpect(jsonPath("$.lastName").value("Parker"));
        }

        @Test
        public void expectUnauthorized_WhenPasswordIsWrong() throws Exception {
            String url = "/v1/student";
            mvc.perform(get(url).with(httpBasic("admin", "wrong-password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(readFileAsString("student.json")))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.errorList[0].title").value(INVALID_CREDENTIALS))
                    .andExpect(jsonPath("$.errorList[0].code").value(SC_UNAUTHORIZED));
        }

        @Test
        public void expectForbidden_WhenApiUserCallPostStudent() throws Exception {
            String url = "/v1/student";
            mvc.perform(post(url).with(httpBasic("apiuser", "password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(readFileAsString("student.json")))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.errorList[0].title").value(FORBIDDEN))
                    .andExpect(jsonPath("$.errorList[0].code").value(SC_FORBIDDEN));
        }

        @Test
        public void expectBadRequest_WhenStudentIdNotProvided() throws Exception {
            String url = "/v1/student";
            mvc.perform(post(url).with(httpBasic("admin", "password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(readFileAsString("invalid_student.json")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                    .andExpect(jsonPath("$.errorList[0].code").value(String.valueOf(SC_BAD_REQUEST)));
        }

        @Test
        public void expectBadRequest_WhenInvalidJsonPayLoad() throws Exception {
            String url = "/v1/student";
            mvc.perform(post(url).with(httpBasic("admin", "password"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(readFileAsString("invalid_json_payload.json")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                    .andExpect(jsonPath("$.errorList[0].code").value(String.valueOf(SC_BAD_REQUEST)))
                    .andExpect(jsonPath("$.errorList[0].detail").value("Invalid JSON request payload"));
        }
    }
}
