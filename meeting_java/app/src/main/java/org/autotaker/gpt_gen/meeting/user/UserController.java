package org.autotaker.gpt_gen.meeting.user;

import org.autotaker.gpt_gen.meeting.user.dto.UserDetailsDto;
import org.autotaker.gpt_gen.meeting.user.dto.UserProfileResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public interface UserController {

    @GetMapping("/whoami")
    public UserProfileResponse whoami(UserDetailsDto userDetails);

    @GetMapping("/{id}")
    public UserProfileResponse profile(Long id);
}
