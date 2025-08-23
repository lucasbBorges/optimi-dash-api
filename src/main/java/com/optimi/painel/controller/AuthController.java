package com.optimi.painel.controller;

import com.optimi.painel.controller.model.auth.LoginRequestDto;
import com.optimi.painel.controller.model.auth.RegisterRequestDTO;
import com.optimi.painel.controller.model.auth.ResponseDTO;
import com.optimi.painel.domain.model.auth.User;
import com.optimi.painel.domain.repository.UserRepository;
import com.optimi.painel.core.securtiy.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginDto) {
        User user = repository.findByEmail(loginDto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO registerDto) {
        Optional<User> user = repository.findByEmail(registerDto.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(registerDto.email());
            newUser.setName(registerDto.name());
            newUser.setPassword(passwordEncoder.encode(registerDto.password()));
            newUser.setRole("USER");
            repository.save(newUser);
            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/test")
    public String teste() {
        return "ok";
    }
}
