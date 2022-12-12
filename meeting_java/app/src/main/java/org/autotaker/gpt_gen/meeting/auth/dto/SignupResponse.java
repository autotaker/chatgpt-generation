package org.autotaker.gpt_gen.meeting.auth.dto;

import lombok.Value;
import org.autotaker.gpt_gen.meeting.common.AppResponse;

@Value
public class SignupResponse implements AppResponse {
    String message;
}
