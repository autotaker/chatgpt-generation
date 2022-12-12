package org.autotaker.gpt_gen.meeting.common;

import lombok.Value;

@Value
public class ErrorResponse implements AppResponse {
    ErrorCode errorCode;
    String message;
}
