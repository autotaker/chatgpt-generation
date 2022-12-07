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

    /**
     * 予約を作成する
     * 
     * @param managerId       予約管理者ID
     * @param reservationForm 予約情報
     * @return 予約情報
     * @throws ServiceException 予約管理者が見つからない場合
     */
    public Reservation createReservation(Long managerId, ReservationForm reservationForm) throws ServiceException {
        Optional<ReservationManager> reservationManager = reservationManagerRepository.findById(managerId);
        if (!reservationManager.isPresent()) {
            throw new ServiceException(new ErrorResponse(ErrorCode.RESERVATION_MANAGER_NOT_FOUND, "予約管理者が見つかりません"));
        }
        Reservation reservation = new Reservation(reservationForm.getName(),
                reservationForm.getEmail(),
                reservationForm.getPhoneNumber(),
                reservationForm.getTimeSlot(),
                reservationForm.getPurpose(),
                reservationManager.get());
        reservationRepository.save(reservation);
        return reservation;
    }

    /**
     * 予約管理者を作成する
     * 
     * @param reservationManagerForm 予約管理者のフォーム
     * @return 作成した予約管理者
     * @throws ServiceException 予約管理者が既に存在している場合
     */
    public ReservationManager createReservationManager(ReservationManagerForm reservationManagerForm)
            throws ServiceException {
        Optional<ReservationManager> reservationManager = reservationManagerRepository
                .findByEmail(reservationManagerForm.getEmail());
        if (reservationManager.isPresent()) {
            throw new ServiceException(
                    new ErrorResponse(ErrorCode.RESERVATION_MANAGER_ALREADY_EXISTS, "予約管理者が既に存在します"));
        }
        ReservationManager newReservationManager = new ReservationManager(reservationManagerForm.getName(),
                reservationManagerForm.getEmail(),
                reservationManagerForm.getPhoneNumber());
        reservationManagerRepository.save(newReservationManager);
        return newReservationManager;
    }
}
