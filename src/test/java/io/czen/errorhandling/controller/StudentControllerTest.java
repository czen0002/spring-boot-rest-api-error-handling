package io.czen.errorhandling.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
public class StudentControllerTest {

    private static final String BAD_REQUEST = "BAD_REQUEST";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("USER")
    public void getStudentSuccessfully() throws Exception {
        String url = "/v1/student?studentId=10";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(SC_ACCEPTED))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.firstName").value("Wayne"))
                .andExpect(jsonPath("$.lastName").value("Bruce"));
    }

    @Test
    @WithMockUser("USER")
    public void getStudentThrowMethodArgumentTypeMismatchException() throws Exception {
        String url = "/v1/student?studentId=1.5";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(SC_BAD_REQUEST))
                .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.errorList[0].code").value("400"))
                .andExpect(jsonPath("$.errorList[0].detail").value("Parameter 'studentId' " +
                        "is invalid. Expected a valid java.lang.Long, but received class java.lang.String."));
    }

    @Test
    @WithMockUser("USER")
    public void getStudentThrowConstraintViolationException() throws Exception {
        String url = "/v1/student?studentId=0";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(SC_BAD_REQUEST))
                .andExpect(jsonPath("$.errorList[0].title").value(BAD_REQUEST))
                .andExpect(jsonPath("$.errorList[0].code").value("400"))
                .andExpect(jsonPath("$.errorList[0].detail")
                        .value("must be greater than or equal to 1"));
    }

    @Test
    public void getStudentThrow401Exception() throws Exception {
        String url = "/v1/student?studentId=0";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(SC_UNAUTHORIZED));
    }
}
