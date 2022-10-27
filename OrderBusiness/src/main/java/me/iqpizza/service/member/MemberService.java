package me.iqpizza.service.member;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.jwt.JwtProvider;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.config.security.dto.AuthenticateUserStorage;
import me.iqpizza.domain.member.dto.LoginDto;
import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.member.repository.MemberRepository;
import me.iqpizza.domain.member.ro.LoginRO;
import me.iqpizza.domain.member.ro.MemberRO;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final AuthenticateUserStorage authenticateUserStorage;
    private final JwtProvider jwtProvider;

    public MemberRO register(final SignDto signDto) {
        if (memberRepository.existsByUsername(signDto.getUsername())) {
            throw new Member.AlreadyExistsUsernameException();
        }

        try {
            Member.MemberRole memberRole = Member.MemberRole.convertFrom(signDto.getRole());
            return new MemberRO(memberRepository.save(signDto.toEntity(memberRole, encoder)));
        } catch (IllegalArgumentException e) {
            throw new Member.NotDefinedRoleException();
        }
    }

    @Transactional(readOnly = true)
    public LoginRO login(final LoginDto loginDto) {
        Member member = memberRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(Member.NotFoundException::new);
        if (!encoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new Member.UnauthorizedException();
        }

        AuthenticateUser authenticateUser =
                new AuthenticateUser(member.getId(), member.getUsername(), member.getRole().name());
        authenticateUserStorage.addUser(authenticateUser);
        String accessToken = jwtProvider.generateAccessToken(authenticateUser);
        String refreshToken = jwtProvider.generateRefreshToken(authenticateUser);
        return new LoginRO(accessToken, refreshToken, jwtProvider.getExpirationSecond());
    }

    public MemberRO findById(final long userId) {
        return new MemberRO(findMemberById(userId));
    }

    @Transactional(readOnly = true)
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Member findMemberById(final long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(Member.NotFoundException::new);
    }
}
