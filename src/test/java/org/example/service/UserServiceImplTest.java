    package org.example.service;

    import org.example.data.repository.UserRepository;
    import org.example.dto.request.LoginRequest;
    import org.example.dto.request.RegisterRequest;
    import org.example.dto.response.LoginResponse;
    import org.example.dto.response.RegisterResponse;
    import org.example.enums.AccountType;
    import org.example.exception.EmailException;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.ActiveProfiles;

    import static org.junit.jupiter.api.Assertions.*;
    @SpringBootTest
    @ActiveProfiles("test")
    class UserServiceImplTest {

        @Autowired
        UserRepository userRepository;

        @BeforeEach
        void setUp() {
            userRepository.deleteAll();
        }

        @Autowired
        UserServiceImpl userService;

        @Test
        public void testToRegister(){
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstName");
            registerRequest.setLastName("lastName");
            registerRequest.setMiddleName("middleName");
            registerRequest.setPin("1234");
            registerRequest.setPhoneNumber("09137217467");
            registerRequest.setEmail("okakafavour81@gmail.com");
            registerRequest.setPassword("123456");

            RegisterResponse response = userService.register(registerRequest);
            assertTrue(response.getMessage().equalsIgnoreCase("Registration successful. check your email to confirm"));
        }

        @Test
        public void testToRegisterWithEmailFormat(){
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("sam");
            registerRequest.setMiddleName("dave");
            registerRequest.setLastName("joshua");
            registerRequest.setEmail("email");
            registerRequest.setPassword("1234");

            assertThrows(EmailException.class, () -> {
                userService.register(registerRequest);
            });

        }

        @Test
        public void testToLogin() {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName("firstName");
            registerRequest.setLastName("LastName");
            registerRequest.setMiddleName("MiddleName");
            registerRequest.setPin("1234");
            registerRequest.setEmail("okakafavour81@gmail.com");
            registerRequest.setPhoneNumber("07015705372");
            registerRequest.setPassword("123456");

            registerRequest.setAccountType(AccountType.SAVINGS);

            userService.register(registerRequest);

            LoginRequest request = new LoginRequest();
            request.setPassword("123456");
            request.setEmail("okakafavour81@gmail.com");

            LoginResponse response = userService.login(request);
            assertEquals("Login Successfully", response.getMessage());
        }

    }