package me.iqpizza.domain.member.repository;

import me.iqpizza.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

}
