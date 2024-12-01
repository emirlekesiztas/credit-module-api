package com.emirhan.ingcasestudy.service;

import com.emirhan.ingcasestudy.dto.AuthenticationRequest;
import com.emirhan.ingcasestudy.dto.AuthenticationResponse;
import com.emirhan.ingcasestudy.dto.RegisterRequest;
import com.emirhan.ingcasestudy.entity.User;
import com.emirhan.ingcasestudy.entity.UserRole;
import com.emirhan.ingcasestudy.repository.UserRepository;
import com.emirhan.ingcasestudy.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", "FIRST_NAME", "LAST_NAME");
        User user = User.builder()
                .email(request.getEmail())
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt_token");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
    }


    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        User user = new User(1L, request.getUserName(), "encoded_password", UserRole.CUSTOMER);

        when(userRepository.findByEmail(request.getUserName())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("jwt_token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
    }

    @Test
    void testAuthenticate_Failure_InvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrong_password");
        AuthenticationException authenticationException = new BadCredentialsException("Bad credentials");

        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(authenticationException);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals("Bad credentials", exception.getMessage());
    }

}