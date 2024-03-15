package com.imaginnovate.service;

import com.imaginnovate.dto.EmployeeDTO;
import com.imaginnovate.entities.Employee;
import com.imaginnovate.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        // Save employee to database
        return employeeRepository.save(employee);
    }

    public List<EmployeeDTO> getTaxDeductionForCurrentYear() {
        // Get the start and end dates for the current financial year (April to March)
        LocalDate currentYearStart = LocalDate.now().withMonth(4).withDayOfMonth(1); // April 1st
        LocalDate currentYearEnd = LocalDate.now().plusYears(1).withMonth(3).withDayOfMonth(31); // March 31st of next year

        // Fetch employees hired within the current financial year
        List<Employee> employees = employeeRepository.findAll();

        // Calculate tax deductions for each employee
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            BigDecimal yearlySalary = calculateYearlySalary(employee, currentYearStart, currentYearEnd);
            BigDecimal taxAmount = calculateTaxAmount(yearlySalary);
            BigDecimal cessAmount = calculateCessAmount(yearlySalary);

            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setEmployeeId(employee.getEmployeeId());
            employeeDTO.setFirstName(employee.getFirstName());
            employeeDTO.setLastName(employee.getLastName());
            employeeDTO.setYearlySalary(yearlySalary);
            employeeDTO.setTaxAmount(taxAmount);
            employeeDTO.setCessAmount(cessAmount);

            employeeDTOs.add(employeeDTO);
        }

        return employeeDTOs;
    }

    public BigDecimal calculateYearlySalary(Employee employee, LocalDate currentYearStart, LocalDate currentYearEnd) {
        LocalDate doj = employee.getDoj();
        LocalDate dojAdjusted = doj.getDayOfMonth() > 15 ? doj.plusMonths(1) : doj;

        // Adjust current year start if employee joined after it
        LocalDate adjustedCurrentYearStart = dojAdjusted.isAfter(currentYearStart) ? dojAdjusted : currentYearStart;

        // Calculate total months in the current financial year
        long totalMonths = ChronoUnit.MONTHS.between(adjustedCurrentYearStart, currentYearEnd) + 1;

        // Calculate total salary for the current financial year
        return employee.getSalary().multiply(BigDecimal.valueOf(totalMonths));
    }

    private BigDecimal calculateTaxAmount(BigDecimal yearlySalary) {
        BigDecimal taxAmount = BigDecimal.ZERO;

        if (yearlySalary.compareTo(BigDecimal.valueOf(250000)) > 0) {
            BigDecimal remainingSalary = yearlySalary.subtract(BigDecimal.valueOf(250000));
            if (remainingSalary.compareTo(BigDecimal.valueOf(250000)) <= 0) {
                taxAmount = taxAmount.add(remainingSalary.multiply(BigDecimal.valueOf(0.05)));
            } else {
                taxAmount = taxAmount.add(BigDecimal.valueOf(12500)); // Tax for first 250000
                remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(250000));

                if (remainingSalary.compareTo(BigDecimal.valueOf(500000)) <= 0) {
                    taxAmount = taxAmount.add(remainingSalary.multiply(BigDecimal.valueOf(0.1)));
                } else {
                    taxAmount = taxAmount.add(BigDecimal.valueOf(50000)); // Tax for next 500000
                    remainingSalary = remainingSalary.subtract(BigDecimal.valueOf(500000));

                    taxAmount = taxAmount.add(remainingSalary.multiply(BigDecimal.valueOf(0.2))); // Tax for remaining amount
                }
            }
        }

        return taxAmount;
    }

    private BigDecimal calculateCessAmount(BigDecimal yearlySalary) {
        BigDecimal cessAmount = BigDecimal.ZERO;

        if (yearlySalary.compareTo(BigDecimal.valueOf(2500000)) > 0) {
            BigDecimal remainingSalary = yearlySalary.subtract(BigDecimal.valueOf(2500000));
            cessAmount = remainingSalary.multiply(BigDecimal.valueOf(0.02));
        }

        return cessAmount;
    }
}

