package me.iqpizza.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class LoginDto {

    @NotBlank
    @Size(min = 4, max = 255)
    private String username;

    @NotBlank
    @Size(min = 4, max = 255)
    private String password;

}
