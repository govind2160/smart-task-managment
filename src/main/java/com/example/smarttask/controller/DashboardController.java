package com.example.smarttask.controller;

import com.example.smarttask.dto.DashboardStatsDto;
import com.example.smarttask.security.CustomUserDetails;
import com.example.smarttask.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // GET /dashboard
    @GetMapping
    public ResponseEntity<DashboardStatsDto> getDashboardStats(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        DashboardStatsDto stats = dashboardService.getDashboardStats(userDetails.getUser());
        return ResponseEntity.ok(stats);
    }
}
