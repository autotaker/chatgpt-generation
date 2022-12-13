package org.autotaker.gpt_gen.meeting.user;

import org.autotaker.gpt_gen.meeting.user.value.UserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordFactoryImpl implements UserPasswordFactory {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserPassword create(String plainPassword) {
        return new UserPassword(passwordEncoder.encode(plainPassword));
    }
}
