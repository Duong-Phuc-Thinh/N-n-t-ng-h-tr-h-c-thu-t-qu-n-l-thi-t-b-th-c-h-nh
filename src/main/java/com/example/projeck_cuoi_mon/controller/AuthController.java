package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.LoginRequest;
import com.example.projeck_cuoi_mon.dto.request.RegisterRequest;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.repository.DepartmentRepository;
import com.example.projeck_cuoi_mon.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DepartmentRepository departmentRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        addDepartments(model);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest registerRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            addDepartments(model);
            return "auth/register";
        }

        try {
            SessionUser loginUser = authService.register(registerRequest);
            session.setAttribute(SessionConstants.LOGIN_USER, loginUser);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            addDepartments(model);
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequest loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            SessionUser loginUser = authService.login(loginRequest);
            session.setAttribute(SessionConstants.LOGIN_USER, loginUser);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private void addDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
    }
}
