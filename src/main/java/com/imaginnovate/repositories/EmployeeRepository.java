package com.imaginnovate.repositories;

import com.imaginnovate.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDojBetween(LocalDate startDate, LocalDate endDate);

}
