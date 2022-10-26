package me.iqpizza.service.member;

import me.iqpizza.config.jwt.JwtProvider;
import me.iqpizza.config.security.dto.AuthenticateUserStorage;
import me.iqpizza.domain.member.dto.LoginDto;
import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.member.repository.MemberRepository;
import me.iqpizza.domain.member.ro.LoginRO;
import me.iqpizza.domain.member.ro.MemberRO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

    @Spy
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mock
    private AuthenticateUserStorage authenticateUserStorage;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private MemberService memberService;

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
        final SignDto signDto = new SignDto("username", "password", "customer");
        final Member member = signDto.toEntity(Member.MemberRole.CUSTOMER, encoder);
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

    @Test
    @DisplayName("사용자 이름이 틀린 경우, 로그인 실패")
    void loginFailedByWrongUsername() {
        // given
        final LoginDto loginDto = new LoginDto("username", "password");

        // when
        when(memberRepository.findByUsername(anyString()))
                .thenThrow(new Member.NotFoundException());

        // then
        assertThrows(Member.NotFoundException.class, () ->
                memberService.login(loginDto)
        );
    }
    
    @Test
    @DisplayName("사용자 비밀번호가 일치하지 않는 경우, 로그인 실패")
    void loginFailedByInvalidPassword() {
        // given
        final Member member = Member.builder()
                .username("username")
                .password("12345678")
                .role(Member.MemberRole.CUSTOMER)
                .build();
        final LoginDto loginDto = new LoginDto("username", "password");
        given(memberRepository.findByUsername(anyString()))
                .willReturn(Optional.of(member));

        // when
        assertThrows(Member.UnauthorizedException.class, () ->
            memberService.login(loginDto)
        );
    }
    
    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() {
        // given
        final Member member = Member.builder()
                .id(1L)
                .username("username")
                .password(encoder.encode("password"))
                .build();
        final LoginDto loginDto = new LoginDto("username", "password");
        given(memberRepository.findByUsername(anyString()))
                .willReturn(Optional.of(member));
        given(jwtProvider.generateAccessToken(any()))
                .willReturn("12345678900987654321");
        given(jwtProvider.generateRefreshToken(any()))
                .willReturn("09876543211234567890");

        // when
        LoginRO loginRO = memberService.login(loginDto);

        // then
        assertThat(loginRO).isNotNull();
        assertThat(loginRO.getAccessToken()).isNotNull();
        assertThat(loginRO.getRefreshToken()).isNotNull();
    }

}
