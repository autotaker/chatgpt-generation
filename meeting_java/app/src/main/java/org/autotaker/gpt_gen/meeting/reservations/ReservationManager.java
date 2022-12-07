package org.autotaker.gpt_gen.meeting.reservations;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ReservationManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReservationManager(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
