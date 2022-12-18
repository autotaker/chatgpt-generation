package org.autotaker.gpt_gen.meeting.user.value;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    USER,
    ADMIN;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : UserRole.values()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            if (this == role) {
                break;
            }
        }
        return authorities;
    }
}
