package org.autotaker.gpt_gen.meeting.user;

import java.util.Optional;

import org.autotaker.gpt_gen.meeting.common.ErrorCode;
import org.autotaker.gpt_gen.meeting.common.NotFoundException;
import org.autotaker.gpt_gen.meeting.user.dto.UserDetailsDto;
import org.autotaker.gpt_gen.meeting.user.dto.UserProfileResponse;
import org.autotaker.gpt_gen.meeting.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;

@RestController
public class UserControllerImpl implements UserController {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserProfileResponse whoami(@AuthenticationPrincipal UserDetailsDto userDetails) {

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, "ユーザが見つかりません"));
        return user.toUserProfileResponse();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse profile(@PathVariable("id") Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, "ユーザが見つかりません"));
        return user.toUserProfileResponse();
    }

}
