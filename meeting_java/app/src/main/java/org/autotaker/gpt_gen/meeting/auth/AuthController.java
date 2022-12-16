package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.auth.dto.SignupRequest;
import org.autotaker.gpt_gen.meeting.common.AppResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthController {
    @PostMapping(path = "/signup", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    HttpEntity<AppResponse> signup(SignupRequest request);
}
