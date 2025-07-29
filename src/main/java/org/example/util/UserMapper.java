package org.example.util;

import org.example.data.model.Account;
import org.example.data.model.User;
import org.example.data.repository.AccountRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.RegisterRequest;
import org.example.dto.response.RegisterResponse;
import org.example.enums.AccountType;
import org.example.exception.UserAlreadyExist;
import org.example.service.AccountServiceImpl;
import org.example.service.BankServiceImpl;
import org.example.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailServiceImpl  emailService;


    public User mapToRegisterRequest(RegisterRequest registerRequest) {
        User user = createUser(registerRequest);
        User savedUser = userRepository.save(user);

        generateVerificationToken(savedUser);
        User updatedUser = userRepository.save(savedUser);

        sendVerificationEmail(updatedUser);
        return updatedUser;
    }



    public RegisterResponse mapToRegisterResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        registerResponse.setMessage("Registration successful. check your email to confirm");
        return registerResponse;
    }


    public User createUser(RegisterRequest registerRequest) {
        System.out.println("Incoming raw password: " + registerRequest.getPassword());
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setMiddleName(registerRequest.getMiddleName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(PasswordHashingMapper.hashPassword(registerRequest.getPassword()));
        user.setVerified(false);
        user.setTokenExpiryDate(registerRequest.getTokenExpiryDate());
        user.setVerificationToken(UUID.randomUUID().toString());
        return user;
    }

    public void generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
    }

    public void sendVerificationEmail(User user) {
        String link = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), link);
    }

}
