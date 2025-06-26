package com.example.MaintenanceHelpdesk.user_service.service;

import com.example.MaintenanceHelpdesk.user_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MaintenanceHelpdesk.user_service.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User register(User user) {
        return repository.save(user);
    }

    public User login(String email, String password) {
        return repository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
    public List<User> getServiceProviders(String keyword, String serviceType) {
    List<User> providers = repository.findByRole(User.Role.SERVICE_PROVIDER);

    if (keyword != null && !keyword.isEmpty()) {
        providers = providers.stream()
            .filter(p -> p.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                         p.getServiceType().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    if (serviceType != null && !serviceType.isEmpty()) {
        providers = providers.stream()
            .filter(p -> p.getServiceType().equalsIgnoreCase(serviceType))
            .collect(Collectors.toList());
    }

    return providers;
}
    public User getUserById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
}
}
