package org.autotaker.gpt_gen.meeting.reservations;

public class ErrorResponse implements AppResponse {
    private final String status = "error";
    private final ErrorCode errorCode;
    private final String reason;

    public ErrorResponse(ErrorCode errorCode, String reason) {
        this.errorCode = errorCode;
        this.reason = reason;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", errorCode, reason);
    }
}
