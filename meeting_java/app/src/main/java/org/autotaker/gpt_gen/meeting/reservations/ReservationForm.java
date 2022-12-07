package org.autotaker.gpt_gen.meeting.reservations;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationForm {
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime timeSlot;
    private String purpose;
}
