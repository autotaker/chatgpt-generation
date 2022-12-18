package org.autotaker.gpt_gen.meeting.prelude;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class Prelude {
    @Bean
    public Algorithm jwtAlgorithm() {
        return new JwtAlgorithmFactory().create();
    }

}
