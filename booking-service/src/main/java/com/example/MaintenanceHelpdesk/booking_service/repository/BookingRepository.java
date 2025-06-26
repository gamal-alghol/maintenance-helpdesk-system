
package com.example.MaintenanceHelpdesk.booking_service.repository;

import com.example.MaintenanceHelpdesk.booking_service.entity.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findByProviderId(Long providerId);
 List<Booking> findByRequesterId(Long requesterId);

}
