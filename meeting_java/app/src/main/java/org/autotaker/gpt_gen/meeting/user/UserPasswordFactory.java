package org.autotaker.gpt_gen.meeting.user;

import org.autotaker.gpt_gen.meeting.user.value.UserPassword;

public interface UserPasswordFactory {
    UserPassword create(String plainPassword);
}
