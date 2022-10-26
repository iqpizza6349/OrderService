package me.iqpizza.service.member;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.member.repository.MemberRepository;
import me.iqpizza.domain.member.ro.MemberRO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

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
    public MemberRO findById(final long userId) {
        return new MemberRO(memberRepository.findById(userId)
                .orElseThrow(Member.NotFoundException::new));
    }
}
