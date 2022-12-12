package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.user.entity.User;

public interface UserService {

    boolean exists(User user);

}
