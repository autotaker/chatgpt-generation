package org.autotaker.gpt_gen.meeting.user;

import org.autotaker.gpt_gen.meeting.user.dto.UserDetailsDto;
import org.autotaker.gpt_gen.meeting.user.entity.User;
import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(new UserEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new UserDetailsDto(user);
    }

}
