package com.example.MaintenanceHelpdesk.user_service.controller;

import com.example.MaintenanceHelpdesk.user_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.MaintenanceHelpdesk.user_service.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }
@GetMapping("/{id}")
public User getUserById(@PathVariable Long id) {
    return service.getUserById(id);
}
    @PostMapping("/login")
    public User login(@RequestParam String email, @RequestParam String password) {
        return service.login(email, password);
    }
    
     @GetMapping("/providers")
public List<User> getAllServiceProviders(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String filterByServiceType) {

    return service.getServiceProviders(keyword, filterByServiceType);
}
}
