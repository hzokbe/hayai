package com.hzokbe.hayai.auth.service.user;

import com.hzokbe.hayai.auth.dto.jwt.JWTResponseDTO;
import com.hzokbe.hayai.auth.dto.user.SignInRequestDTO;
import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.dto.user.UserCreatedEventDTO;
import com.hzokbe.hayai.auth.exception.user.EmailAlreadyInUseException;
import com.hzokbe.hayai.auth.exception.user.UserIsNotActiveException;
import com.hzokbe.hayai.auth.exception.user.UserNotFoundException;
import com.hzokbe.hayai.auth.exception.user.UsernameAlreadyInUseException;
import com.hzokbe.hayai.auth.mapper.user.UserEntityMapper;
import com.hzokbe.hayai.auth.repository.user.UserRepository;
import com.hzokbe.hayai.auth.service.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserEntityMapper mapper;

    private final KafkaTemplate<String, UserCreatedEventDTO> kafkaTemplate;

    private final JWTService jwtService;

    public void signUp(SignUpRequestDTO dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyInUseException("username already in use");
        }

        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyInUseException("e-mail already in use");
        }

        var user = mapper.toEntity(dto);

        user = repository.save(user);

        kafkaTemplate.send("users.created", new UserCreatedEventDTO(user.getId()));
    }

    public JWTResponseDTO signIn(SignInRequestDTO dto) {
        var optionalUser = repository.findByUsername(dto.username());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("user not found");
        }

        var user = optionalUser.get();

        if (!user.isActive()) {
            throw new UserIsNotActiveException("user is not active");
        }

        return new JWTResponseDTO(jwtService.generateJWT(user.getId().toString()));
    }
}
