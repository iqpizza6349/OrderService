package me.iqpizza.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.member.entity.Member;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class SignDto {

    @NotBlank
    @Size(min = 4, max = 255)
    private String username;

    @NotBlank
    @Size(min = 4, max = 255)
    private String password;

    @Nullable
    private String role;

    public Member toEntity(Member.MemberRole role, PasswordEncoder encoder) {
        return Member.builder()
                .username(username)
                .password(encoder.encode(password))
                .role(role)
                .build();
    }
}
