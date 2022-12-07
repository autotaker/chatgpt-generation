package org.autotaker.gpt_gen.meeting.reservations;

public class ReservationManagerResponse implements AppResponse {
    private final String status = "ok";
    private final long reservationManagerId;

    public ReservationManagerResponse(long reservationManagerId) {
        this.reservationManagerId = reservationManagerId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public long getReservationManagerId() {
        return reservationManagerId;
    }
}
