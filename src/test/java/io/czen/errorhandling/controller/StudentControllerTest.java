package io.czen.errorhandling.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;

@WebMvcTest
public class StudentControllerTest {

    private static final String GET_STUDENT_PATH = "/v1/student";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void getStudentSuccessfully() throws Exception {
        String url = "/v1/student?studentId=10";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .with(csrf())).andExpect(status().is(SC_ACCEPTED));
    }
}
