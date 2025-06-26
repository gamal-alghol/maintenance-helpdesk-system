package com.example.MaintenanceHelpdesk.booking_service.controller;

import com.example.MaintenanceHelpdesk.booking_service.entity.Booking;
import com.example.MaintenanceHelpdesk.booking_service.entity.Booking.BookingStatus;
import com.example.MaintenanceHelpdesk.booking_service.service.BookingService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.saveBooking(booking);
    }
@PutMapping("/{id}/status")
public ResponseEntity<?> updateBookingStatus(@PathVariable Long id,
                                             @RequestParam BookingStatus status) {
    boolean updated = bookingService.updateBookingStatus(id, status);
    if (updated) {
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}
@GetMapping("/by-provider/{providerId}")
public List<Booking> getBookingsByProvider(@PathVariable Long providerId) {
    return bookingService.getBookingsByProvider(providerId);
}
@GetMapping("/by-requester/{requesterId}")
public List<Booking> getBookingsByRequester(@PathVariable Long requesterId) {
    return bookingService.getBookingsByRequester(requesterId);
}

}
