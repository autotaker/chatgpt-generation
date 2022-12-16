package org.autotaker.gpt_gen.meeting.reservations;

import org.autotaker.gpt_gen.meeting.reservations.dto.AppResponse;
import org.autotaker.gpt_gen.meeting.reservations.dto.ReservationForm;
import org.autotaker.gpt_gen.meeting.reservations.dto.ReservationManagerForm;
import org.autotaker.gpt_gen.meeting.reservations.dto.ReservationManagerResponse;
import org.autotaker.gpt_gen.meeting.reservations.dto.ReservationResponse;
import org.autotaker.gpt_gen.meeting.reservations.entity.Reservation;
import org.autotaker.gpt_gen.meeting.reservations.entity.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    /**
     * 予約を作成する
     * 
     * @param managerId       予約管理者ID
     * @param reservationForm 予約情報
     * @return 予約ID
     */
    @PostMapping(value = "/reservations", consumes = { "application/json", "application/x-www-form-urlencoded" })
    public ResponseEntity<AppResponse> createReservation(@RequestParam("manager_id") Long managerId,
            ReservationForm reservationForm) {
        try {
            Reservation reservation = reservationService.createReservation(managerId, reservationForm);
            return ResponseEntity.ok(new ReservationResponse(reservation.getReservationId()));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponse());
        }
    }

    /**
     * 予約管理者を作成する
     * 
     * @param reservationManagerForm 予約管理者情報
     * @return 予約管理者ID
     */
    @PostMapping(value = "/managers", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<AppResponse> createReservationManager(
            ReservationManagerForm reservationManagerForm) {
        try {
            ReservationManager reservationManager = reservationService
                    .createReservationManager(reservationManagerForm);
            return ResponseEntity.ok(new ReservationManagerResponse(reservationManager.getManagerId()));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponse());
        }
    }

}
