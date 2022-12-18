package org.autotaker.gpt_gen.meeting.auth.dto;

import lombok.Value;

@Value
public class SigninResponse {
    private final String accessToken;
    private final String refreshToken;
}
