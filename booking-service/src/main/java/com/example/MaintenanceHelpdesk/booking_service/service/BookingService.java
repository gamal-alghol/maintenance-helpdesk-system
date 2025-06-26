package com.example.MaintenanceHelpdesk.booking_service.service;

import com.example.MaintenanceHelpdesk.booking_service.entity.Booking;
import com.example.MaintenanceHelpdesk.booking_service.entity.Booking.BookingStatus;
import com.example.MaintenanceHelpdesk.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

      @Autowired
    private BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    
     public boolean updateBookingStatus(Long id, BookingStatus status) {
    Optional<Booking> bookingOptional = bookingRepository.findById(id);
    if (bookingOptional.isPresent()) {
        Booking booking = bookingOptional.get();
        booking.setStatus(status);
        bookingRepository.save(booking);
        return true;
    }
    return false;
}
public List<Booking> getBookingsByProvider(Long providerId) {
    return bookingRepository.findByProviderId(providerId);
}
public List<Booking> getBookingsByRequester(Long requesterId) {
    return bookingRepository.findByRequesterId(requesterId);
}
}
