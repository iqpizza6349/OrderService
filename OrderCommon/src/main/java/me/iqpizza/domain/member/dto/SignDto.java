package me.iqpizza.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.iqpizza.domain.member.entity.Member;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class SignDto {

    @NotBlank
    @Size(min = 4, max = 255)
    private String username;

    @Nullable
    private String role;

    public Member toEntity(Member.MemberRole role) {
        return Member.builder()
                .username(username)
                .role(role)
                .build();
    }
}
