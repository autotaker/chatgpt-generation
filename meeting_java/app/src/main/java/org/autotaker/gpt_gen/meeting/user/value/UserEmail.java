package org.autotaker.gpt_gen.meeting.user.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmail {
    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", unique = true)
    @JsonProperty("email")
    private String value;
}
