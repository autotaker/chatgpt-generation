package org.autotaker.gpt_gen.meeting.reservations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationManagerRepository reservationManagerRepository;

    public ReservationResponse createReservation(Long managerId, ReservationForm reservationForm) {
        Optional<ReservationManager> reservationManager = reservationManagerRepository.findById(managerId);
        if (!reservationManager.isPresent()) {
            return new ReservationErrorResponse(ErrorCode.RESERVATION_MANAGER_NOT_FOUND, "予約管理者が見つかりません");
        }
        Reservation reservation = new Reservation(reservationForm.getName(),
                reservationForm.getEmail(),
                reservationForm.getPhoneNumber(),
                reservationForm.getTimeSlot(),
                reservationForm.getPurpose(),
                reservationManager.get());
        reservationRepository.save(reservation);
        return new ReservationSuccessResponse(reservation.getReservationId());
    }
}
