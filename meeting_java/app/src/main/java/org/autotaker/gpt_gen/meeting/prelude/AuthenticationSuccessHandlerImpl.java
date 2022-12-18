package org.autotaker.gpt_gen.meeting.prelude;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

import org.autotaker.gpt_gen.meeting.auth.dto.SigninResponse;
import org.autotaker.gpt_gen.meeting.user.dto.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private Algorithm algorithm;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserDetailsDto user = (UserDetailsDto) authentication.getPrincipal();
        Instant issuedAt = Instant.now();
        Instant notBefore = issuedAt.minus(10, ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plus(1L, ChronoUnit.HOURS);

        String token = JWT.create()
                .withIssuedAt(issuedAt)
                .withNotBefore(notBefore)
                .withExpiresAt(expiresAt)
                .withSubject("access_token")
                .withClaim("https://api.autotaker.org/auth", user.toClaim())
                .sign(algorithm);
        String refreshToken = "dummy";

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        SigninResponse signinResponse = new SigninResponse(token, refreshToken);

        response.getWriter().write(new ObjectMapper().writeValueAsString(signinResponse));
    }

}
