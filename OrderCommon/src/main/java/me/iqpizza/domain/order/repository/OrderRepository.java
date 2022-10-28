package me.iqpizza.domain.order.repository;

import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndMember(Long id, Member member);

    Page<Order> findAllByMember(Member member, Pageable pageable);

}
