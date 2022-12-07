package org.autotaker.gpt_gen.meeting.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public AppResponse createReservation(@RequestParam("manager_id") Long managerId,
            @RequestBody ReservationForm reservationForm) {
        try {
            Reservation reservation = reservationService.createReservation(managerId, reservationForm);
            return new ReservationResponse(reservation.getReservationId());
        } catch (ErrorResponse e) {
            return e;
        }
    }

    @PostMapping(value = "/managers", consumes = { "application/json", "application/x-www-form-urlencoded" })
    public AppResponse createReservationManager(@RequestBody ReservationManagerForm reservationManagerForm) {
        try {
            ReservationManager reservationManager = reservationService
                    .createReservationManager(reservationManagerForm);
            return new ReservationManagerResponse(reservationManager.getManagerId());
        } catch (ErrorResponse e) {
            return e;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationController.class, args);
    }
}
