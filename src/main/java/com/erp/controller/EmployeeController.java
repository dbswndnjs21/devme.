package com.erp.controller;

import com.erp.domain.dto.UserDto;
import com.erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;

    @GetMapping("/employees")
    public String employee() {
        return "employeeList";
    }

    @GetMapping("/api/employees/list")
    public ResponseEntity<List<UserDto>> employeeList() {
        List<UserDto> userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }
}


