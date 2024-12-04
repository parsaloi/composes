package com.example.controller;

import com.example.service.LargestRectangleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LargestRectangleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidMatrix() throws Exception {
        int[][] validMatrix = {
            {1, 0, 1, 0},
            {1, 0, 1, 1},
            {1, 1, 1, 1},
            {1, 0, 0, 1}
        };

        mockMvc.perform(post("/largest-rectangle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validMatrix)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.largestRectangleArea").value(4));
    }

    @Test
    public void testInvalidMatrix() throws Exception {
        int[][] invalidMatrix = {
            {1, 0, 2},
            {1, 1, 1}
        };

        mockMvc.perform(post("/largest-rectangle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMatrix)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error: Matrix should contain only 0's and 1's"));
    }
}
