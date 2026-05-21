package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.model.enums.UserRole;
import com.example.projeck_cuoi_mon.service.DashboardService;
import com.example.projeck_cuoi_mon.service.EquipmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final EquipmentService equipmentService;
    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        if (loginUser.getRole() == UserRole.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        if (loginUser.getRole() == UserRole.LECTURER) {
            return "redirect:/lecturer/dashboard";
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("dashboard", dashboardService.getStudentDashboard(loginUser.getId()));
        return "dashboard/student";
    }

    @GetMapping("/lecturer/dashboard")
    public String lecturerDashboard(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("dashboard", dashboardService.getLecturerDashboard(loginUser.getId()));
        return "dashboard/lecturer";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        model.addAttribute("loginUser", session.getAttribute(SessionConstants.LOGIN_USER));
        model.addAttribute("stats", equipmentService.getDashboardStats());
        return "dashboard/admin";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "error/403";
    }
}
