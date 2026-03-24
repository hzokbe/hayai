package com.hzokbe.hayai.auth.dto.user;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserCreatedEventDTO {
    private final UUID id;

    private final Timestamp createdAt = Timestamp.from(Instant.now());

    public UserCreatedEventDTO(UUID id) {
        this.id = id;
    }
}
