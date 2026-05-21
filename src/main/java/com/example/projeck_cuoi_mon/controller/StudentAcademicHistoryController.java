package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.service.AcademicHistoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StudentAcademicHistoryController {

    private final AcademicHistoryService academicHistoryService;

    @GetMapping("/student/academic-history")
    public String academicHistory(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("history", academicHistoryService.getStudentAcademicHistory(loginUser.getId()));
        return "student/academic-history/index";
    }
}
