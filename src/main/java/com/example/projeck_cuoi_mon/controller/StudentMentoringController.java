package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.CancelMentoringForm;
import com.example.projeck_cuoi_mon.dto.request.MentoringBookingForm;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.repository.DepartmentRepository;
import com.example.projeck_cuoi_mon.repository.LecturerRepository;
import com.example.projeck_cuoi_mon.repository.MajorRepository;
import com.example.projeck_cuoi_mon.service.MentoringService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student/mentoring")
public class StudentMentoringController {

    private final MentoringService mentoringService;
    private final DepartmentRepository departmentRepository;
    private final LecturerRepository lecturerRepository;
    private final MajorRepository majorRepository;

    @GetMapping({"", "/", "/book"})
    public String bookForm(Model model) {
        model.addAttribute("mentoringBookingForm", new MentoringBookingForm());
        addFormData(model);
        return "student/mentoring/book";
    }

    @GetMapping({"/history", "/list", "/bookings"})
    public String list(HttpSession session, Model model) {
        SessionUser loginUser = getLoginUser(session);
        model.addAttribute("bookings", mentoringService.findStudentBookings(loginUser.getId()));
        model.addAttribute("cancelForm", new CancelMentoringForm());
        return "student/mentoring/list";
    }

    @PostMapping("/book")
    public String book(
            @Valid @ModelAttribute MentoringBookingForm mentoringBookingForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addFormData(model);
            return "student/mentoring/book";
        }

        try {
            SessionUser loginUser = getLoginUser(session);
            mentoringService.book(loginUser.getId(), mentoringBookingForm);
            redirectAttributes.addFlashAttribute("success", "Mentoring session booked successfully");
            return "redirect:/student/mentoring/history";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            addFormData(model);
            return "student/mentoring/book";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancel(
            @PathVariable Long id,
            @Valid @ModelAttribute CancelMentoringForm cancelForm,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Cancel reason must be at most 500 characters");
            return "redirect:/student/mentoring/history";
        }

        try {
            SessionUser loginUser = getLoginUser(session);
            mentoringService.cancel(loginUser.getId(), id, cancelForm.getReason());
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/student/mentoring/history";
    }

    private void addFormData(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("majors", majorRepository.findAllWithDepartment());
        model.addAttribute("lecturers", lecturerRepository.findAllWithUserAndDepartment());
        model.addAttribute("timeSlots", mentoringService.getTimeSlots());
        model.addAttribute("today", LocalDate.now());
    }

    private SessionUser getLoginUser(HttpSession session) {
        return (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
    }
}
