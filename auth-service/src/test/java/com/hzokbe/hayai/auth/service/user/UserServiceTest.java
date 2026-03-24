package com.hzokbe.hayai.auth.service.user;

import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.dto.user.UserCreatedEventDTO;
import com.hzokbe.hayai.auth.entity.user.UserEntity;
import com.hzokbe.hayai.auth.exception.user.EmailAlreadyInUseException;
import com.hzokbe.hayai.auth.exception.user.UsernameAlreadyInUseException;
import com.hzokbe.hayai.auth.mapper.user.UserEntityMapper;
import com.hzokbe.hayai.auth.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

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

    @InjectMocks
    private UserService service;

    private final SignUpRequestDTO dto = new SignUpRequestDTO(
            "ayumu_kasuga",
            "ayumu_kasuga@azumangadaioh.jp",
            "ayumu!123"
    );

    private final UserEntity user = new UserEntity(
            UUID.randomUUID(),
            false,
            true,
            "ayumu_kasuga",
            "ayumu_kasuga@azumangadaioh.jp",
            "$argon2id$v=19$m=16384,t=2,p=1$nplna1LbRVVQhL6NiN3GrQ$Kz6vZG0jW1Eh77UGokKjewPqHEyNCeet+tu2AqEkdOA"
    );

    @Test
    public void signUp_shouldThrowException_whenUsernameAlreadyInUse() {
        when(repository.existsByUsername(dto.username())).thenReturn(true);

        var exception = assertThrows(UsernameAlreadyInUseException.class, () -> service.signUp(dto));

        assertEquals("username already in use", exception.getMessage());

        verify(repository, times(0)).save(any(UserEntity.class));

        verify(kafkaTemplate, times(0)).send(anyString(), any(UserCreatedEventDTO.class));
    }

    @Test
    public void signUp_shouldThrowException_whenEmailAlreadyInUse() {
        when(repository.existsByUsername(dto.username())).thenReturn(false);

        when(repository.existsByEmail(dto.email())).thenReturn(true);

        var exception = assertThrows(EmailAlreadyInUseException.class, () -> service.signUp(dto));

        assertEquals("e-mail already in use", exception.getMessage());

        verify(repository, times(0)).save(any(UserEntity.class));

        verify(kafkaTemplate, times(0)).send(anyString(), any(UserCreatedEventDTO.class));
    }

    @Test
    public void signUp_shouldSaveUser() {
        when(repository.existsByUsername(dto.username())).thenReturn(false);

        when(repository.existsByEmail(dto.email())).thenReturn(false);

        when(mapper.toEntity(dto)).thenReturn(user);

        when(repository.save(any(UserEntity.class))).thenReturn(user);

        service.signUp(dto);

        verify(repository).save(user);

        verify(kafkaTemplate, times(1)).send(anyString(), any(UserCreatedEventDTO.class));
    }
}

