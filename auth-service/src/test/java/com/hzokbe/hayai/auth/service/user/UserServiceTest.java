package com.hzokbe.hayai.auth.service.user;

import com.hzokbe.hayai.auth.dto.user.SignInRequestDTO;
import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.dto.user.UserCreatedEventDTO;
import com.hzokbe.hayai.auth.entity.user.UserEntity;
import com.hzokbe.hayai.auth.exception.user.EmailAlreadyInUseException;
import com.hzokbe.hayai.auth.exception.user.UserIsNotActiveException;
import com.hzokbe.hayai.auth.exception.user.UserNotFoundException;
import com.hzokbe.hayai.auth.exception.user.UsernameAlreadyInUseException;
import com.hzokbe.hayai.auth.mapper.user.UserEntityMapper;
import com.hzokbe.hayai.auth.repository.user.UserRepository;
import com.hzokbe.hayai.auth.service.jwt.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private UserEntityMapper mapper;

    @Mock
    private KafkaTemplate<String, UserCreatedEventDTO> kafkaTemplate;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService service;

    private final SignUpRequestDTO signUpDTO = new SignUpRequestDTO(
            "ayumu_kasuga",
            "ayumu_kasuga@azumangadaioh.jp",
            "ayumu!123"
    );

    private final SignInRequestDTO signInDTO = new SignInRequestDTO(
            "ayumu_kasuga",
            "ayumu!123"
    );

    private final UserEntity activeUser = new UserEntity(
            UUID.randomUUID(),
            false,
            true,
            "ayumu_kasuga",
            "ayumu_kasuga@azumangadaioh.jp",
            "$argon2id$v=19$m=16384,t=2,p=1$nplna1LbRVVQhL6NiN3GrQ$Kz6vZG0jW1Eh77UGokKjewPqHEyNCeet+tu2AqEkdOA"
    );

    private final UserEntity notActiveUser = new UserEntity(
            UUID.randomUUID(),
            false,
            false,
            "ayumu_kasuga",
            "ayumu_kasuga@azumangadaioh.jp",
            "$argon2id$v=19$m=16384,t=2,p=1$nplna1LbRVVQhL6NiN3GrQ$Kz6vZG0jW1Eh77UGokKjewPqHEyNCeet+tu2AqEkdOA"
    );

    @Test
    public void signUp_shouldThrowException_whenUsernameAlreadyInUse() {
        when(repository.existsByUsername(signUpDTO.username())).thenReturn(true);

        var exception = assertThrows(UsernameAlreadyInUseException.class, () -> service.signUp(signUpDTO));

        assertEquals("username already in use", exception.getMessage());

        verify(repository, times(0)).save(any(UserEntity.class));

        verify(kafkaTemplate, times(0)).send(anyString(), any(UserCreatedEventDTO.class));
    }

    @Test
    public void signUp_shouldThrowException_whenEmailAlreadyInUse() {
        when(repository.existsByUsername(signUpDTO.username())).thenReturn(false);

        when(repository.existsByEmail(signUpDTO.email())).thenReturn(true);

        var exception = assertThrows(EmailAlreadyInUseException.class, () -> service.signUp(signUpDTO));

        assertEquals("e-mail already in use", exception.getMessage());

        verify(repository, times(0)).save(any(UserEntity.class));

        verify(kafkaTemplate, times(0)).send(anyString(), any(UserCreatedEventDTO.class));
    }

    @Test
    public void signUp_shouldSaveUser() {
        when(repository.existsByUsername(signUpDTO.username())).thenReturn(false);

        when(repository.existsByEmail(signUpDTO.email())).thenReturn(false);

        when(mapper.toEntity(signUpDTO)).thenReturn(activeUser);

        when(repository.save(any(UserEntity.class))).thenReturn(activeUser);

        service.signUp(signUpDTO);

        verify(repository).save(activeUser);

        verify(kafkaTemplate, times(1)).send(anyString(), any(UserCreatedEventDTO.class));
    }

    @Test
    public void signIn_shouldThrowException_whenUserNotFound() {
        when(repository.findByUsername(signInDTO.username())).thenReturn(Optional.empty());

        var exception = assertThrows(UserNotFoundException.class, () -> service.signIn(signInDTO));

        assertEquals("user not found", exception.getMessage());

        verify(repository).findByUsername(signInDTO.username());

        verify(jwtService, times(0)).generateJWT(activeUser.getId().toString());
    }

    @Test
    public void signIn_shouldThrowException_whenUserIsNotActive() {
        when(repository.findByUsername(signInDTO.username())).thenReturn(Optional.of(notActiveUser));

        var exception = assertThrows(UserIsNotActiveException.class, () -> service.signIn(signInDTO));

        assertEquals("user is not active", exception.getMessage());

        verify(repository).findByUsername(signInDTO.username());

        verify(jwtService, times(0)).generateJWT(activeUser.getId().toString());
    }

    @Test
    public void signIn_shouldReturnJWT() {
        when(repository.findByUsername(signInDTO.username())).thenReturn(Optional.of(activeUser));

        service.signIn(signInDTO);

        verify(repository).findByUsername(signInDTO.username());

        verify(jwtService).generateJWT(activeUser.getId().toString());
    }
}

