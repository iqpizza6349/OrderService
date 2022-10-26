package me.iqpizza.domain;

import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("회원가입, 이름이 굉장히 짧아 실패한 케이스")
    void registerFailedByTooShortUsername() {
        // given
        final SignDto signDto = new SignDto("u", "p", null);

        // when
        Set<ConstraintViolation<SignDto>> violations = validator.validate(signDto);

        // then
        assertThat(violations.isEmpty()).isFalse();
    }
    
    @Test
    @DisplayName("회원가입, 정의되지 않은 권한 요청하여 실패한 케이스")
    void registerFailedByNotDefinedRole() {
        // given
        final SignDto signDto = new SignDto("username", "password", "super_user");

        // when
        assert signDto.getRole() != null;
        assertThrows(Member.NotDefinedRoleException.class, () ->
            Member.MemberRole.convertFrom(signDto.getRole())
        );
    }

    @Test
    @DisplayName("회원가입, 성공적으로 완료된 케이스")
    void registerSuccess() {
        // given
        final SignDto signDto = new SignDto("username", "password", "staff");

        // when
        Set<ConstraintViolation<SignDto>> violations = validator.validate(signDto);
        Member.MemberRole.convertFrom(signDto.getRole());

        // then
        assertThat(violations.isEmpty()).isTrue();
    }
}
