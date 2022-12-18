package org.autotaker.gpt_gen.meeting.user.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.autotaker.gpt_gen.meeting.user.value.UserPassword;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsDto implements UserDetails {
    private Long userId;
    private UserEmail email;
    private UserPassword password;
    private Collection<? extends GrantedAuthority> authorities;
    public static String CLAIM_DOMAIN = "https://api.autotaker.org/auth";

    public UserDetailsDto(Map<String, Object> claims) {
        this.userId = ((Integer) claims.get("user_id")).longValue();
        this.email = new UserEmail((String) claims.get("email"));
        this.password = null;
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Object authority : (List<?>) claims.get("authorities")) {
            authorities.add(new SimpleGrantedAuthority((String) authority));
        }
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password.getValue();
    }

    @Override
    public String getUsername() {
        return email.getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public Map<String, ?> toClaim() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", getUserId());
        map.put("email", getUsername());
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        map.put("authorities", authorities);
        return map;
    }
}
