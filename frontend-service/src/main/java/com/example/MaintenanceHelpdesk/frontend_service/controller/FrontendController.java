/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.MaintenanceHelpdesk.frontend_service.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Controller
public class FrontendController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
     @GetMapping("/logout")
    public String logoutPage(HttpSession session) {
          session.invalidate();
        return "login";
    }

@PostMapping("/login")
public String loginUser(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
    try {
        Map<String, String> response = restTemplate.postForObject(
            "http://user-service/api/users/login?email=" + email + "&password=" + password,
            null,
            Map.class
        );

        if (response == null || !response.containsKey("role")) {
            model.addAttribute("error", "Invalid response from server");
            return "login";
        }

        String role = response.get("role");
        session.setAttribute("loggedInUser", response); 
        session.setAttribute("fullName", response.get("fullName"));

       if ("SERVICE_REQUESTER".equals(response.get("role"))) {
            return "redirect:/home";
        } else if ("SERVICE_PROVIDER".equals(response.get("role"))) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/unauthorized";
        }

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Login failed: " + e.getMessage());
        return "login";
    }
}



    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

 @PostMapping("/register")
public String registerUser(@RequestParam Map<String, String> formData, Model model) {
    try {
        restTemplate.postForObject("http://user-service/api/users/register", formData, Map.class);
        return "redirect:/login";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "register";  
    }
}

@GetMapping("/home")
public String homePage(@RequestParam(required = false) String search,
                       Model model,
                       HttpSession session) {

    Map<String, Object> user = (Map<String, Object>) session.getAttribute("loggedInUser");

    if (user == null || !"SERVICE_REQUESTER".equals(user.get("role"))) {
        return "redirect:/unauthorized";
    }
 model.addAttribute("fullName", session.getAttribute("fullName")); 
    String url = "http://user-service/api/users/providers";
    if (search != null && !search.isEmpty()) {
        url += "?search=" + search;
    }

    List<Map<String, Object>> providers = restTemplate.getForObject(url, List.class);

    Map<String, List<Map<String, Object>>> groupedProviders = new TreeMap<>();
    for (Map<String, Object> provider : providers) {
        String serviceType = (String) provider.get("serviceType");
        if (serviceType == null || serviceType.isEmpty()) serviceType = "Other";
        groupedProviders.computeIfAbsent(serviceType, k -> new ArrayList<>()).add(provider);
    }

    model.addAttribute("colors", List.of("#FFCDD2", "#C8E6C9", "#BBDEFB", "#FFF9C4", "#D1C4E9"));
    model.addAttribute("groupedProviders", groupedProviders);

    return "home";
}
@GetMapping("/book/{providerId}")
public String showBookingPage(@PathVariable String providerId, Model model) {
    Map<String, Object> provider = restTemplate.getForObject("http://user-service/api/users/" + providerId, Map.class);
    model.addAttribute("provider", provider);
    return "booking";
}

@PostMapping("/book")
public String submitBooking(@RequestParam Long providerId,
                            @RequestParam String problemDescription,
                            @RequestParam String visitDate,
                            @RequestParam int expectedHours,
                            HttpSession session,
                            Model model) {
    Map<String, Object> user = (Map<String, Object>) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/login";
    }

    Long requesterId = Long.valueOf(user.get("id").toString()); 

    Map<String, Object> bookingData = new HashMap<>();
    bookingData.put("providerId", providerId);
    bookingData.put("requesterId", requesterId);
    bookingData.put("problemDescription", problemDescription);
    bookingData.put("visitDate", visitDate);
    bookingData.put("expectedHours", expectedHours);

    try {
        restTemplate.postForObject("http://booking-service/api/booking", bookingData, Void.class);
        model.addAttribute("message", "Booking submitted successfully!");
        return "redirect:/home";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Failed to submit booking.");
        return "error"; 
    }
}

@PostMapping("/update-booking-status")
public String updateBookingStatus(@RequestParam Long bookingId,
                                  @RequestParam String status) {
    String url = "http://booking-service/api/booking/" + bookingId + "/status?status=" + status;
    try {
        restTemplate.put(url, null);
    } catch (Exception e) {
        e.printStackTrace(); 
    }
    return "redirect:/dashboard";
}
@GetMapping("/dashboard")
public String providerDashboard(Model model, HttpSession session) {
    Map<String, Object> user = (Map<String, Object>) session.getAttribute("loggedInUser");

    if (user == null || !"SERVICE_PROVIDER".equals(user.get("role"))) {
        return "redirect:/unauthorized";
    }

    try {
        Long providerId = Long.valueOf(user.get("id").toString());
        String url = "http://booking-service/api/booking/by-provider/" + providerId;

        List<Map<String, Object>> bookings = restTemplate.getForObject(url, List.class);

        for (Map<String, Object> booking : bookings) {
            if (booking.get("requesterId") != null) {
                try {
                    Long requesterId = Long.valueOf(booking.get("requesterId").toString());
                    Map<String, Object> requester = restTemplate.getForObject(
                        "http://user-service/api/users/" + requesterId, Map.class);
                    
                    booking.put("requesterName", requester.get("fullName"));  // لإظهاره في الصفحة
                    booking.put("requesterEmail", requester.get("email"));    // إن أردت
                } catch (Exception e) {
                    booking.put("requesterName", "Unknown");
                }
            } else {
                booking.put("requesterName", "Unknown");
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("providerId", providerId);
        model.addAttribute("fullName", session.getAttribute("fullName"));

        model.addAttribute("statuses", List.of("PENDING", "IN_PROGRESS", "REJECTED", "COMPLETED"));

        return "dashboard";

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Failed to load dashboard: " + e.getMessage());
        return "error";
    }
}
@GetMapping("/my-bookings")
public String myBookings(Model model, HttpSession session) {
    Map<String, Object> user = (Map<String, Object>) session.getAttribute("loggedInUser");

    if (user == null || !"SERVICE_REQUESTER".equals(user.get("role"))) {
        return "redirect:/unauthorized";
    }

    try {
        Long requesterId = Long.valueOf(user.get("id").toString());
        String url = "http://booking-service/api/booking/by-requester/" + requesterId;

        List<Map<String, Object>> bookings = restTemplate.getForObject(url, List.class);

        model.addAttribute("bookings", bookings);
        model.addAttribute("fullName", session.getAttribute("fullName"));
        return "my-bookings"; 

    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Failed to load your bookings: " + e.getMessage());
        return "error";
    }
}


}