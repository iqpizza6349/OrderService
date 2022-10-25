package me.iqpizza.domain.member.ro;

import lombok.Getter;
import me.iqpizza.domain.member.entity.Member;

@Getter
public class MemberRO {

    private final Long id;
    private final String username;
    private final Member.MemberRole role;

    public MemberRO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.role = member.getRole();
    }
}
