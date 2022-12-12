package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.auth.dto.SignupRequest;
import org.autotaker.gpt_gen.meeting.auth.dto.SignupResponse;
import org.autotaker.gpt_gen.meeting.common.ConflictException;
import org.autotaker.gpt_gen.meeting.common.ErrorCode;
import org.autotaker.gpt_gen.meeting.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public SignupResponse signup(SignupRequest request) {
        User user = new User(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        if (userService.exists(user)) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS, "既に登録されているユーザです");
        }
        userRepository.save(user);
        return new SignupResponse("ユーザを登録しました");
    }
}
