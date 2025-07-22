package org.example.service;

import org.example.data.model.User;
import org.example.data.repository.UserRepository;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.RegisterResponse;
import org.example.exception.EmailException;
import org.example.exception.UserAlreadyExist;
import org.example.util.JwtUtil;
import org.example.util.PasswordHashingMapper;
import org.example.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static org.example.validation.Validations.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordHashingMapper passwordHashingMapper;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        ValidateRegisterRequest(registerRequest);
        if(userRepository.existsByEmail(registerRequest.getEmail())) throw new EmailException("Email already exist");

       User user =  userMapper.mapToRegisterRequest(registerRequest);
        return userMapper.mapToRegisterResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        if (!userRepository.existsByEmail(loginRequest.getEmail())) throw new UserAlreadyExist("Email already exist");

        boolean passwordMatch = PasswordHashingMapper.checkPassword(loginRequest.getPassword(), PasswordHashingMapper.hashPassword(loginRequest.getPassword()));
        if (!passwordMatch) throw new UserAlreadyExist("Invalid password");

        String token = jwtUtil.generateToken(loginRequest.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setMessage("Login Successfully");
        response.setUserId(loginRequest.getUserId());
        return response;
    }

    @Override
    public boolean verifyToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);

        if (user == null || user.getTokenExpiryDate().isBefore(LocalDateTime.now())) return false;

        user.setVerified(true);
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
        return true;
    }

    public static void ValidateRegisterRequest(RegisterRequest request) {
        request.setFirstName(validateName(request.getFirstName()));
        request.setLastName(validateName(request.getLastName()));
        request.setMiddleName(validateName(request.getMiddleName()));
        request.setEmail(validateEmail(request.getEmail()));
        request.setPassword(PasswordHashingMapper.hashPassword(request.getPassword()));
        request.setPhoneNumber(validatePhoneNumber(request.getPhoneNumber()));
        request.setPin(validateTransferPin(request.getPin()));
    }
}

