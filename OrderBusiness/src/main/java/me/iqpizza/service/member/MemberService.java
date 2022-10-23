package me.iqpizza.service.member;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;



}
