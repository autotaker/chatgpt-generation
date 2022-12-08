package org.autotaker.gpt_gen.meeting.reservations.dto;

public class ReservationResponse implements AppResponse {
    private final String status = "ok";
    private final long reservationId;

    public ReservationResponse(long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public long getReservationId() {
        return reservationId;
    }
}
