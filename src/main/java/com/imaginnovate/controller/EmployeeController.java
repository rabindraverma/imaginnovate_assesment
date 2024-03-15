package com.imaginnovate.controller;

import com.imaginnovate.dto.EmployeeDTO;
import com.imaginnovate.entities.Employee;
import com.imaginnovate.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/tax-deduction")
    public ResponseEntity<List<EmployeeDTO>> getTaxDeductionForCurrentYear() {
        List<EmployeeDTO> taxDeductions = employeeService.getTaxDeductionForCurrentYear();
        return new ResponseEntity<>(taxDeductions, HttpStatus.OK);
    }
}