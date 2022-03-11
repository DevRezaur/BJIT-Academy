package com.devrezaur.integration.controller;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.devrezaur.payload.response.UserInfoResponse;
import com.devrezaur.repository.UserRepository;
import com.devrezaur.service.UserService;
import com.devrezaur.utility.UserInfoRequestBuilder;
import com.devrezaur.utility.UserInfoResponseBuilder;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserInfoRequestBuilder userInfoRequestBuilder;
    @Autowired
    private UserInfoResponseBuilder userInfoResponseBuilder;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserService userServiceMock;

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
        Mockito.when(userServiceMock.findUserByUsername(any(String.class))).thenReturn(userRepository.findByUsernameIgnoreCase(username));

        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, user, String.class);
        return response.getBody();
    }

    @Test
    @DisplayName("Testing Get Personal Info Method")
    void getPersonalInfo() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/user/profile";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("userId"), "11364");
        assertEquals(JSONObject.get("username"), "rahman.rezaur@bjitgroup.com");
        assertEquals(JSONObject.get("fullName"), "Rezaur Rahman");
        assertEquals(JSONObject.get("role"), "ROLE_USER");
    }

    @Test
    @DisplayName("Testing Update Personal Info Method")
    void updatePersonalInfo() throws Exception {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Mr Rezaur");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userServiceMock.updateUserInfo(any(User.class))).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<UserInfoResponse> request = new HttpEntity<>(userInfoResponseBuilder.build(user), headers);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/user/profile/update";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("userId"), "11364");
        assertEquals(JSONObject.get("username"), "rahman.rezaur@bjitgroup.com");
        assertEquals(JSONObject.get("fullName"), "Mr Rezaur");
        assertEquals(JSONObject.get("role"), "ROLE_USER");
    }

    @Test
    @DisplayName("Testing Add New User Data Method")
    void addNewUserData() throws Exception {
        JSONObject tokenResponse = new JSONObject(getToken("saleehin@bjitgroup.com", "iamadmin"));
        String token = (String) tokenResponse.get("token");

        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userServiceMock.updateUserInfo(any(User.class))).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<UserInfoResponse> request = new HttpEntity<>(userInfoResponseBuilder.build(user), headers);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/user/add";
        URI uri = new URI(baseUrl);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("userId"), "11364");
        assertEquals(JSONObject.get("username"), "rahman.rezaur@bjitgroup.com");
        assertEquals(JSONObject.get("fullName"), "Rezaur Rahman");
        assertEquals(JSONObject.get("role"), "ROLE_USER");
    }

    @Test
    void getUserInfoById() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("saleehin@bjitgroup.com", "iamadmin"));
        String token = (String) tokenResponse.get("token");

        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userServiceMock.findUserByUserId("11364")).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/user/profile/{userId}";

        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class, "11364");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONObject JSONObject = new JSONObject(response.getBody());
        assertEquals(JSONObject.get("userId"), "11364");
        assertEquals(JSONObject.get("username"), "rahman.rezaur@bjitgroup.com");
        assertEquals(JSONObject.get("fullName"), "Rezaur Rahman");
        assertEquals(JSONObject.get("role"), "ROLE_USER");
    }

    @Test
    void listAllUsers() throws URISyntaxException, JSONException {
        JSONObject tokenResponse = new JSONObject(getToken("rahman.rezaur@bjitgroup.com", "iamuser"));
        String token = (String) tokenResponse.get("token");

        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userServiceMock.getAllUser()).thenReturn(List.of(user));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        final String baseUrl = "http://localhost:" + randomPort + "/api/v1/user/all";

        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        JSONArray JSONArray = new JSONArray(response.getBody());
        JSONObject JSONObject = JSONArray.getJSONObject(0);
        assertEquals(JSONObject.get("userId"), user.getUserId());
        assertEquals(JSONObject.get("username"), user.getUsername());
        assertEquals(JSONObject.get("fullName"), user.getFullName());
        assertEquals(JSONObject.get("role"), "ROLE_USER");
    }
}