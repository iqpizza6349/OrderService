package me.iqpizza.service.member;

import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.member.repository.MemberRepository;
import me.iqpizza.domain.member.ro.MemberRO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Spy
    private PasswordEncoder encoder;

    @Test
    @DisplayName("유저 이름 중복으로 인한 회원가입 실패")
    void registerFailedTest() {
        // given
        final SignDto signDto = new SignDto("username", "password", null);

        // when
        when(memberRepository.existsByUsername(anyString()))
                .thenThrow(new Member.AlreadyExistsUsernameException());

        // then
        assertThrows(Member.AlreadyExistsUsernameException.class, () ->
            memberService.register(signDto)
        );
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerSuccessTest() {
        // given
        final SignDto signDto = new SignDto("username", "password", null);
        Member member = signDto.toEntity(Member.MemberRole.CUSTOMER, encoder);
        given(memberRepository.existsByUsername(anyString()))
                .willReturn(false);

        // when
        when(memberRepository.save(any(Member.class)))
                .thenReturn(member);
        MemberRO memberRO = memberService.register(signDto);

        // then
        assertThat(memberRO).isNotNull();
        assertThat(memberRO.getUsername()).isEqualTo(signDto.getUsername());
        assertThat(memberRO.getRole()).isEqualTo(Member.MemberRole.CUSTOMER);
    }
}
