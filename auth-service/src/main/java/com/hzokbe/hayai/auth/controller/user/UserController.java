package com.hzokbe.hayai.auth.controller.user;

import com.hzokbe.hayai.auth.dto.jwt.JWTResponseDTO;
import com.hzokbe.hayai.auth.dto.user.SignInRequestDTO;
import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDTO dto) {
        service.signUp(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JWTResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.signIn(dto));
    }
}
