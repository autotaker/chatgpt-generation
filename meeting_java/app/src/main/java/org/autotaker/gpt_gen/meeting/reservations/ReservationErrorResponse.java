package org.autotaker.gpt_gen.meeting.reservations;

public class ReservationErrorResponse implements ReservationResponse {
    private String status;
    private ErrorCode errorCode;
    private String reason;

    public ReservationErrorResponse(ErrorCode errorCode, String reason) {
        this.status = "error";
        this.errorCode = errorCode;
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }
}
