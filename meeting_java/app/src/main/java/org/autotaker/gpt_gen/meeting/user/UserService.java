package org.autotaker.gpt_gen.meeting.user;

import org.autotaker.gpt_gen.meeting.user.entity.User;

public interface UserService {

    boolean exists(User user);

    boolean checkPassword(User user, String password);

}
