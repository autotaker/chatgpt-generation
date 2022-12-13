package org.autotaker.gpt_gen.meeting.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.autotaker.gpt_gen.meeting.user.value.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.autotaker.gpt_gen.meeting.user.value.UserPassword;
import org.autotaker.gpt_gen.meeting.user.value.UserPhoneNumber;
import org.autotaker.gpt_gen.meeting.user.value.UserName;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private UserName name;
    @Embedded
    private UserEmail email;
    @Embedded
    private UserPassword password;
    @Embedded
    private UserPhoneNumber phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public User(UserName name, UserEmail email, UserPassword password, UserPhoneNumber phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = UserRole.USER;
    }
}
