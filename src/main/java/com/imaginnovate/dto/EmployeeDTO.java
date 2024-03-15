package com.imaginnovate.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class EmployeeDTO {
    private String employeeId;
    private String firstName;
    private String lastName;
    private BigDecimal yearlySalary;
    private BigDecimal taxAmount;
    private BigDecimal cessAmount;

}
