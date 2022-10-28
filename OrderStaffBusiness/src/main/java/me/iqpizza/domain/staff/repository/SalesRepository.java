package me.iqpizza.domain.staff.repository;

import me.iqpizza.domain.staff.entity.Sales;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalesRepository extends CrudRepository<Sales, Long> {

    Optional<Sales> findByOrderAt(LocalDate orderAt);

}
