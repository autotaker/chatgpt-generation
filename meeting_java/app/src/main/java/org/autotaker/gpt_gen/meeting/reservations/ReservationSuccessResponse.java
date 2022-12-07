package org.autotaker.gpt_gen.meeting.reservations;

public class ReservationSuccessResponse implements AppResponse {
    private String status;
    private Long id;

    public ReservationSuccessResponse(Long id) {
        this.status = "ok";
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }
}
