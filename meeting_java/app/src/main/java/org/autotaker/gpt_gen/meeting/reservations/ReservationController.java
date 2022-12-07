package org.autotaker.gpt_gen.meeting.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping(value = "/reservations", consumes = { "application/json", "application/x-www-form-urlencoded" })
    public ResponseEntity<AppResponse> createReservation(@RequestParam("manager_id") Long managerId,
            @RequestBody ReservationForm reservationForm) {
        try {
            Reservation reservation = reservationService.createReservation(managerId, reservationForm);
            return ResponseEntity.ok(new ReservationResponse(reservation.getReservationId()));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponse());
        }
    }

    @PostMapping(value = "/managers", consumes = { "application/json", "application/x-www-form-urlencoded" })
    public ResponseEntity<AppResponse> createReservationManager(
            @RequestBody ReservationManagerForm reservationManagerForm) {
        try {
            ReservationManager reservationManager = reservationService
                    .createReservationManager(reservationManagerForm);
            return ResponseEntity.ok(new ReservationManagerResponse(reservationManager.getManagerId()));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponse());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationController.class, args);
    }
}
