package com.clearview.docusign.hackathon.auth.controller;


import com.clearview.docusign.hackathon.auth.entities.RefreshToken;
import com.clearview.docusign.hackathon.auth.entities.User;
import com.clearview.docusign.hackathon.auth.repository.UserRepository;
import com.clearview.docusign.hackathon.auth.service.AuthService;
import com.clearview.docusign.hackathon.auth.service.JwtService;
import com.clearview.docusign.hackathon.auth.service.RefreshTokenService;
import com.clearview.docusign.hackathon.auth.utils.AuthResponse;
import com.clearview.docusign.hackathon.auth.utils.LoginRequest;
import com.clearview.docusign.hackathon.auth.utils.RefreshTokenRequest;
import com.clearview.docusign.hackathon.auth.utils.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, UserRepository userRepository, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }



    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header missing or invalid.");
        }

        // Extract the JWT token from the header
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }




    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshTokenHandler(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());

    }

}



