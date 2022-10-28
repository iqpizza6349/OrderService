package me.iqpizza.domain.staff.service;

import lombok.RequiredArgsConstructor;
import me.iqpizza.config.security.dto.AuthenticateUser;
import me.iqpizza.domain.staff.repository.SalesRepository;
import me.iqpizza.domain.staff.ro.SalesRO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StaffService {

    private final SalesRepository salesRepository;

    public SalesRO getSalesDay(AuthenticateUser user, final LocalDate date) {
        if (!user.getRole().equalsIgnoreCase("STAFF")) {
            // 접근 금지 예외
            throw new IllegalArgumentException();
        }

        return new SalesRO(salesRepository.findByOrderAt(date)
                .orElseThrow());
    }
}
