package org.autotaker.gpt_gen.meeting.user.value;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

/**
 * ハッシュ化されたパスワードを表す。
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {
    @NotBlank
    @Size(max = 255)
    @Column(name = "password")
    private String value;
}
