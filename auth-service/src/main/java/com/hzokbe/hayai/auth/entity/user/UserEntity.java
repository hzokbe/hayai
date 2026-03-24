package com.hzokbe.hayai.auth.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("gen_random_uuid()")
    private UUID id;

    @Column(name = "is_admin", nullable = false)
    @ColumnDefault("false")
    private boolean isAdmin = false;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("true")
    private boolean isActive = true;

    @Column(length = 16, nullable = false, unique = true)
    private String username;

    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    public UserEntity(String username, String email, String passwordHash) {
        this.username = username;

        this.email = email;

        this.passwordHash = passwordHash;
    }
}
