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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Reservation(String name, String email, String phoneNumber, LocalDateTime timeSlot, String purpose,
            ReservationManager manager) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.timeSlot = timeSlot;
        this.purpose = purpose;
        this.managerId = manager.getManagerId();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
