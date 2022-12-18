package org.autotaker.gpt_gen.meeting.user.value;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserName {
    @NotBlank
    @Size(max = 255)
    @Column(name = "name")
    @JsonProperty("name")
    private String value;
}
