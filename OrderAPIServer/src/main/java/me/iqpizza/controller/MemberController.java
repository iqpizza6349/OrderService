package me.iqpizza.controller;

import lombok.RequiredArgsConstructor;
import me.iqpizza.domain.member.dto.LoginDto;
import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.ro.LoginRO;
import me.iqpizza.domain.member.ro.MemberRO;
import me.iqpizza.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberRO registerMember(@RequestBody @Valid SignDto signDto) {
        return memberService.register(signDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginRO loginMember(@RequestBody @Valid LoginDto loginDto) {
        return memberService.login(loginDto);
    }
    
    @GetMapping
    public MemberRO findById(@RequestParam("user-id") long userId) {
        return memberService.findById(userId);
    }
}
