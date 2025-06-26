
package com.example.MaintenanceHelpdesk.user_service.repository;

import com.example.MaintenanceHelpdesk.user_service.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long id);
    
    
    List<User> findByRole(User.Role role);

 
}
