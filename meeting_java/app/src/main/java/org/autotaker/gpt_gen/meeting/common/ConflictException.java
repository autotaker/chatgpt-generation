package org.autotaker.gpt_gen.meeting.common;

import org.springframework.http.HttpStatus;

public class ConflictException extends ServiceException {
    public ConflictException(ErrorCode errorCode, String message) {
        super(HttpStatus.CONFLICT, errorCode, message);
    }
}
