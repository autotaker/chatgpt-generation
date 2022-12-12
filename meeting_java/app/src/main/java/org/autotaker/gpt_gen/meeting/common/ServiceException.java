package org.autotaker.gpt_gen.meeting.common;

import org.springframework.http.HttpStatusCode;

public class ServiceException extends RuntimeException {
    private HttpStatusCode statusCode;
    private ErrorCode errorCode;

    public ServiceException(HttpStatusCode statusCode, ErrorCode errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
