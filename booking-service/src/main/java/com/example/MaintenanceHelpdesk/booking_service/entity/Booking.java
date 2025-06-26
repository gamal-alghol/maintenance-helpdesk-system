
package com.example.MaintenanceHelpdesk.booking_service.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;
    private Long requesterId; 

    private String problemDescription;

    private LocalDate visitDate;

    private int expectedHours;
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private BookingStatus status = BookingStatus.PENDING;
  
    public Booking(Long providerId, String problemDescription, LocalDate visitDate, int expectedHours) {
        this.providerId = providerId;
        this.problemDescription = problemDescription;
        this.visitDate = visitDate;
        this.expectedHours = expectedHours;
        this.status = BookingStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public int getExpectedHours() {
        return expectedHours;
    }

    public void setExpectedHours(int expectedHours) {
        this.expectedHours = expectedHours;
    }
    
    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    
public enum BookingStatus {
    PENDING,
    IN_PROGRESS,
    REJECTED,
    COMPLETED
}
}
