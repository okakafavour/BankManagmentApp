package org.example.controller;

import org.example.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    UserServiceImpl userService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyToken(token);
        if (isVerified) return ResponseEntity.ok("User verified successfully");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
    }
}
