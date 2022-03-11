package com.devrezaur.integration.controller;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.devrezaur.security.models.RefreshToken;
import com.devrezaur.security.service.RefreshTokenService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private RefreshTokenService refreshTokenService;

    @LocalServerPort
    int randomPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing Create Authentication Token for Success Case")
    void testCreateAuthenticationTokenForSuccessCase() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/authenticate";
        URI uri = new URI(baseUrl);

        User user = new User();
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setPassword("iamuser");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, user, String.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(response.getBody()).contains("token"));
        assertTrue(Objects.requireNonNull(response.getBody()).contains("refreshToken"));
    }

    @Test
    @DisplayName("Testing Create Authentication Token for Failure Case")
    void testCreateAuthenticationTokenForFailureCase() throws URISyntaxException, JSONException {
        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/authenticate";
        URI uri = new URI(baseUrl);

        User user = new User();
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setPassword("iamadmin");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, user, String.class);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        JSONObject JSONResponse = new JSONObject(response.getBody());
        assertEquals(JSONResponse.get("errorCode"), "401-login-failed");
        assertEquals(JSONResponse.get("message"), "Invalid credentials!");
    }

    @Test
    @DisplayName("Testing Invalidate Refresh Token for Success Case")
    void testInvalidateForSuccessCase() throws Exception {
        Mockito.when(refreshTokenService.deleteByUserId(any(String.class))).thenReturn(1);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/invalidate";
        URI uri = new URI(baseUrl);

        Map<String, String> request = new HashMap<>();
        request.put("userid", "11364");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Testing Invalidate Refresh Token for Failure Case")
    void testInvalidateForFailureCase() throws Exception {
        Mockito.when(refreshTokenService.deleteByUserId(any(String.class))).thenThrow(new Exception("No Session Found!"));

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/invalidate";
        URI uri = new URI(baseUrl);

        Map<String, String> request = new HashMap<>();
        request.put("userid", "11364");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Testing Get Refresh Token for Success Case")
    void testRefreshTokenForSuccessCase() throws URISyntaxException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenId(1);
        refreshToken.setUser(new User("11364", "rahman.rezaur@bjitgroup.com", "iamuser", List.of(new Role(1, "ROLE_USER")), "Rezaur Rahman", null, null, null, null, null, null));
        refreshToken.setRefreshToken("bda48b9b-4ad4-465d-b51f-ce23e0e6b10a");

        Mockito.when(refreshTokenService.findByRefreshToken("bda48b9b-4ad4-465d-b51f-ce23e0e6b10a")).thenReturn(refreshToken);
        Mockito.when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/refreshToken";
        URI uri = new URI(baseUrl);

        Map<String, String> request = new HashMap<>();
        request.put("refreshToken", "bda48b9b-4ad4-465d-b51f-ce23e0e6b10a");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Testing Get Refresh Token for Failure Case")
    void testRefreshTokenForFailureCase() throws URISyntaxException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenId(1);
        refreshToken.setUser(new User("11364", "rahman.rezaur@bjitgroup.com", "iamuser", List.of(new Role(1, "ROLE_USER")), "Rezaur Rahman", null, null, null, null, null, null));
        refreshToken.setRefreshToken("bda48b9b-4ad4-465d-b51f-ce23e0e6b10a");

        Mockito.when(refreshTokenService.findByRefreshToken("bda48b9b-4ad4-465d-b51f-ce23e0e6b10a")).thenReturn(null);
        Mockito.when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/auth/refreshToken";
        URI uri = new URI(baseUrl);

        Map<String, String> request = new HashMap<>();
        request.put("refreshToken", "bda48b9b-4ad4-465d-b51f-ce23e0e6b10a");

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}