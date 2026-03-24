package com.hzokbe.hayai.auth.repository.user;

import com.hzokbe.hayai.auth.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
