package org.autotaker.gpt_gen.meeting.common;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(HttpStatus.NOT_FOUND, errorCode, message);
    }

}
