package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.auth.dto.SignupRequest;
import org.autotaker.gpt_gen.meeting.common.AppResponse;
import org.autotaker.gpt_gen.meeting.common.ErrorResponse;
import org.autotaker.gpt_gen.meeting.common.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    @Autowired
    private AuthService authService;

    @Override
    public HttpEntity<AppResponse> signup(SignupRequest request) {
        try {
            return ResponseEntity.ok(authService.signup(request));
        } catch (ServiceException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
        }
    }
}
