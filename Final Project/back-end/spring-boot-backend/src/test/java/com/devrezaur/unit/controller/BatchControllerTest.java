package com.devrezaur.unit.controller;

import com.devrezaur.controller.BatchController;
import com.devrezaur.model.Batch;
import com.devrezaur.payload.response.BatchResponse;
import com.devrezaur.security.jwt.JwtRequestFilter;
import com.devrezaur.security.service.UserDetailsServiceImplementation;
import com.devrezaur.service.BatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BatchController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "rahman.rezaur@bjitgroup.com", password = "iamuser", roles = "USER")
class BatchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserDetailsServiceImplementation userDetailsServiceImplementation;
    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private BatchService batchService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing Get All Batches for Success Case")
    void getAllBatchesForSuccessCase() throws Exception {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getAllBatches()).thenReturn(List.of(batchResponse));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/batch/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].batchId").value(3))
                .andExpect(jsonPath("$[0].batchName").value("Java Batch 01"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Testing Get Enrolled Batches for Success Case")
    void getEnrolledBatchesForSuccessCase() throws Exception {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getEnrolledBatches(anyString())).thenReturn(List.of(batchResponse));

        Map<String, String> request = new HashMap<>();
        request.put("userid", "11364");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/batch/batches")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].batchId").value(3))
                .andExpect(jsonPath("$[0].batchName").value("Java Batch 01"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Testing Get Batch By Id for Not Null Case")
    void getBatchByIdForNotNullCase() throws Exception {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getBatchById(3)).thenReturn(batchResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/batch/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.batchId").value(3))
                .andExpect(jsonPath("$.batchName").value("Java Batch 01"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Testing Get Batch By Id for Null Case")
    void getBatchByIdForNullCase() throws Exception {
        Mockito.when(batchService.getBatchById(3)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/batch/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Testing Update Existing Batch for Success Case")
    @WithMockUser(username = "saleehin@bjitgroup.com", password = "iamadmin", roles = "ADMIN")
    void updateBatchForSuccessCase() throws Exception {
        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.editBatch(any(Batch.class))).thenReturn(batchResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/batch/update")
                        .content(objectMapper.writeValueAsString(batchResponse))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.batchId").value(3))
                .andExpect(jsonPath("$.batchName").value("Java Batch 01"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Testing Update Existing Batch for Failure Case")
    @WithMockUser(username = "saleehin@bjitgroup.com", password = "iamadmin", roles = "ADMIN")
    void updateBatchFoFailureCase() throws Exception {
        Mockito.when(batchService.editBatch(any(Batch.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/batch/update")
                        .content(objectMapper.writeValueAsString(new BatchResponse()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}