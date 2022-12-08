package org.autotaker.gpt_gen.meeting.reservations;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime timeSlot;
    private String purpose;
    private Long managerId;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    @Builder
    private Reservation(String name, String email, String phoneNumber, LocalDateTime timeSlot, String purpose,
            ReservationManager manager) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.timeSlot = timeSlot;
        this.purpose = purpose;
        this.managerId = manager.getManagerId();
    }
}
