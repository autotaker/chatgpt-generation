package org.autotaker.gpt_gen.meeting.reservations;

public class ServiceException extends Exception {
    private ErrorResponse errorResponse;

    public ServiceException(ErrorResponse errorResponse) {
        super(errorResponse.toString());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
