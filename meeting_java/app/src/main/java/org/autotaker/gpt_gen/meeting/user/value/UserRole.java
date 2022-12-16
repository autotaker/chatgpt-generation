package org.autotaker.gpt_gen.meeting.user.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    USER,
    ADMIN;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : UserRole.values()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
            if (this == role) {
                break;
            }
        }
        return authorities;
    }
}
