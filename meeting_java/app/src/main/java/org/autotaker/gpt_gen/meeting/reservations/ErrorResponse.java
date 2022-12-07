package org.autotaker.gpt_gen.meeting.reservations;

public class ErrorResponse extends Exception implements AppResponse {
    private final String status = "error";
    private final ErrorCode errorCode;
    private final String reason;

    public ErrorResponse(ErrorCode errorCode, String reason) {
        super(errorCode + ": " + reason);
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
}
