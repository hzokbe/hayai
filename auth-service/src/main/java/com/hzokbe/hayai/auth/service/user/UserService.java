package com.hzokbe.hayai.auth.service.user;

import com.hzokbe.hayai.auth.dto.user.SignUpRequestDTO;
import com.hzokbe.hayai.auth.exception.user.EmailAlreadyInUseException;
import com.hzokbe.hayai.auth.exception.user.UsernameAlreadyInUseException;
import com.hzokbe.hayai.auth.mapper.user.UserEntityMapper;
import com.hzokbe.hayai.auth.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserEntityMapper mapper;

    public void signUp(SignUpRequestDTO dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyInUseException("username already in use");
        }

        if (repository.existsByEmail(dto.email())) {
            throw new EmailAlreadyInUseException("e-mail already in use");
        }

        var user = mapper.toEntity(dto);

        repository.save(user);
    }
}
