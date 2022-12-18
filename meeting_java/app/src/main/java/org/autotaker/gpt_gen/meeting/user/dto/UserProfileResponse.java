package org.autotaker.gpt_gen.meeting.user.dto;

import org.autotaker.gpt_gen.meeting.user.value.UserEmail;
import org.autotaker.gpt_gen.meeting.user.value.UserName;
import org.autotaker.gpt_gen.meeting.user.value.UserPhoneNumber;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {

    private long userId;
    @JsonUnwrapped
    private UserName name;
    @JsonUnwrapped
    private UserEmail email;
    @JsonUnwrapped(enabled = false)
    private UserPhoneNumber phoneNumber;

}
