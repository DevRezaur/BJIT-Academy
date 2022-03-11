package com.devrezaur.integration.controller;

import com.devrezaur.model.Batch;
import com.devrezaur.model.User;
import com.devrezaur.payload.response.BatchResponse;
import com.devrezaur.service.BatchService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BatchControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private BatchService batchService;

    @LocalServerPort
    int randomPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String getToken(String username, String password) throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/authenticate";
        URI uri = new URI(baseUrl);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, user, String.class);
        return response.getBody();
    }

    @Test
    @DisplayName("Testing Get All Batches Method")
    void getAllBatches() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getAllBatches()).thenReturn(List.of(batchResponse));

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/batch/all";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONArray JSONArray = new JSONArray(response.getBody());
        JSONObject JSONObject = JSONArray.getJSONObject(0);
        assertEquals(JSONObject.get("batchId"), batchResponse.getBatchId());
        assertEquals(JSONObject.get("batchName"), batchResponse.getBatchName());
        assertEquals(JSONObject.get("description"), batchResponse.getDescription());
        assertEquals(JSONObject.get("imageUrl"), batchResponse.getImageUrl());
    }

    @Test
    @DisplayName("Testing Get Enrolled Batches Method")
    void getEnrolledBatches() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getEnrolledBatches(any(String.class))).thenReturn(List.of(batchResponse));

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/batch/batches";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        Map<String, String> map = new HashMap<>();
        map.put("userid", "11364");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONArray JSONArray = new JSONArray(response.getBody());
        JSONObject JSONObject = JSONArray.getJSONObject(0);
        assertEquals(JSONObject.get("batchId"), batchResponse.getBatchId());
        assertEquals(JSONObject.get("batchName"), batchResponse.getBatchName());
        assertEquals(JSONObject.get("description"), batchResponse.getDescription());
        assertEquals(JSONObject.get("imageUrl"), batchResponse.getImageUrl());
    }

    @Test
    @DisplayName("Testing Get Batch By Id Method")
    void getBatchById() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.getBatchById(any(Integer.class))).thenReturn(batchResponse);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/batch/{batchId}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class, "3");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("batchId"), batchResponse.getBatchId());
        assertEquals(JSONObject.get("batchName"), batchResponse.getBatchName());
        assertEquals(JSONObject.get("description"), batchResponse.getDescription());
        assertEquals(JSONObject.get("imageUrl"), batchResponse.getImageUrl());
    }

    @Test
    @DisplayName("Testing Add New Batch Method")
    void addNewBatch() throws Exception {
        JSONObject tokenResponse = new JSONObject(getToken("saleehin@bjitgroup.com", "iamadmin"));
        String token = (String) tokenResponse.get("token");

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setBatchId(3);
        batchResponse.setBatchName("Java Batch 01");
        batchResponse.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batchResponse.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        Mockito.when(batchService.addBatch(any(Batch.class))).thenReturn(batchResponse);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/batch/add";

        Batch batch = new Batch();
        batch.setBatchId(3);
        batch.setBatchName("Java Batch 01");
        batch.setDescription("This is sample description of Java batch 01. This batch started at 1 October 2021. And expects to end at 31 December, 2021.");
        batch.setImageUrl("https://devrezaur.com/File-Bucket/image/spring.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Batch> request = new HttpEntity<>(batch, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("batchId"), batchResponse.getBatchId());
        assertEquals(JSONObject.get("batchName"), batchResponse.getBatchName());
        assertEquals(JSONObject.get("description"), batchResponse.getDescription());
        assertEquals(JSONObject.get("imageUrl"), batchResponse.getImageUrl());
    }
}