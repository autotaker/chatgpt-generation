package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.auth.dto.SignupRequest;
import org.autotaker.gpt_gen.meeting.auth.dto.SignupResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest request);
}
