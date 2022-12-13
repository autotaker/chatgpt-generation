package org.autotaker.gpt_gen.meeting.auth;

import org.autotaker.gpt_gen.meeting.auth.dto.SignupRequest;
import org.autotaker.gpt_gen.meeting.auth.dto.SignupResponse;
import org.autotaker.gpt_gen.meeting.common.ConflictException;
import org.autotaker.gpt_gen.meeting.common.ErrorCode;
import org.autotaker.gpt_gen.meeting.user.UserRepository;
import org.autotaker.gpt_gen.meeting.user.UserService;
import org.autotaker.gpt_gen.meeting.user.entity.User;
import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.autotaker.gpt_gen.meeting.user.value.UserPassword;
import org.autotaker.gpt_gen.meeting.user.value.UserPhoneNumber;
import org.autotaker.gpt_gen.meeting.user.value.UserName;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;

    public AuthServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public SignupResponse signup(SignupRequest request) {
        User user = new User(
                new UserName(request.getName()),
                new UserEmail(request.getEmail()),
                new UserPassword(request.getPassword()),
                new UserPhoneNumber(request.getPhoneNumber()));
        if (userService.exists(user)) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS, "既に登録されているメールアドレスです");
        }
        userRepository.save(user);
        return new SignupResponse("ユーザを登録しました");
    }
}
