package com.devrezaur.controller;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.devrezaur.payload.response.HttpErrorResponse;
import com.devrezaur.payload.response.JwtResponse;
import com.devrezaur.payload.response.RefreshTokenResponse;
import com.devrezaur.security.jwt.JwtUtil;
import com.devrezaur.security.models.RefreshToken;
import com.devrezaur.security.models.UserDetailsImplementation;
import com.devrezaur.security.service.RefreshTokenService;
import com.devrezaur.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {
        Authentication auth = null;

        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new HttpErrorResponse("401-login-failed", "Invalid credentials!"));
        }

        UserDetailsImplementation myUserDetails = (UserDetailsImplementation) auth.getPrincipal();
        final String jwt = jwtUtil.generateToken(myUserDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(myUserDetails.getUserId());
        List<String> roles = myUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return ResponseEntity.status(200).body(new JwtResponse("Bearer", jwt, refreshToken.getRefreshToken(), myUserDetails.getUserId(), myUserDetails.getUsername(), myUserDetails.getFullName(), roles));
    }

    @PostMapping("/invalidate")
    public ResponseEntity<?> invalidate(@RequestBody Map<String, String> userid) {
        try {
            refreshTokenService.deleteByUserId(userid.get("userid"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex);
        }
        return ResponseEntity.status(200).body("User logged out!");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> refreshToken) {
        RefreshToken token = refreshTokenService.findByRefreshToken(refreshToken.get("refreshToken"));

        if (token != null && refreshTokenService.verifyExpiration(token) != null) {
            User user = token.getUser();
            Map<String, Object> claims = new HashMap<>();
            claims.put("ROLES", user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));
            String jwt = jwtUtil.createToken(claims, user.getUsername());

            return ResponseEntity.status(201).body(new RefreshTokenResponse("Bearer", jwt, refreshToken.get("refreshToken")));
        }

        return ResponseEntity.status(401).body(new HttpErrorResponse("401-invalid-token", "Refresh token invalid!"));
    }

}
