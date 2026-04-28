package adaii.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import adaii.dto.AuthResponse;
import adaii.dto.LoginRequest;
import adaii.dto.RegisterRequest;
import adaii.entity.User;
import adaii.exception.EmailAlreadyExistException;
import adaii.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return  AuthResponse.builder()
                .message("User registered successfully")
                .build();
    }

    // add login service
    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("user not found"));

        String token = jwtService.generateToken(user.getEmail(),user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .message("Login Successful")
                .status(200)
                .build();
    }




}
