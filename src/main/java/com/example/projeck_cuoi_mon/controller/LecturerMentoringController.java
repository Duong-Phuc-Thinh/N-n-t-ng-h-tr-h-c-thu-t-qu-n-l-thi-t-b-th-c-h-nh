package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.CompleteMentoringForm;
import com.example.projeck_cuoi_mon.dto.request.EditMentoringScheduleForm;
import com.example.projeck_cuoi_mon.dto.request.EquipmentLendItem;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.model.enums.EvaluationGrade;
import com.example.projeck_cuoi_mon.repository.EquipmentRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lecturer")
public class LecturerMentoringController {

    private final MentoringService mentoringService;
    private final EquipmentRepository equipmentRepository;

    @GetMapping("/mentoring-history")
    public String mentoringHistory(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("sessions", mentoringService.findLecturerBookings(loginUser.getId()));
        return "lecturer/mentoring-history";
    }

    @GetMapping("/mentoring-history/{id}/complete")
    public String completeForm(@PathVariable Long id, HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
        
        CompleteMentoringForm form = new CompleteMentoringForm();
        List<EquipmentLendItem> lendItems = equipmentRepository.findActiveWithLabRoomType().stream()
                .map(eq -> new EquipmentLendItem(
                        eq.getId(),
                        eq.getName(),
                        eq.getLabRoomType().getName(),
                        eq.getQuantityAvailable(),
                        eq.getQuantityAvailable() > 0 ? 1 : 0,
                        false
                ))
                .collect(Collectors.toList());
        form.setItems(lendItems);

        model.addAttribute("completeMentoringForm", form);
        model.addAttribute("grades", EvaluationGrade.values());
        return "lecturer/complete-mentoring";
    }

    @GetMapping("/mentoring-history/{id}/edit")
    public String editScheduleForm(@PathVariable Long id, HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
        model.addAttribute("editMentoringScheduleForm", mentoringService.getEditScheduleForm(loginUser.getId(), id));
        model.addAttribute("timeSlots", mentoringService.getTimeSlots());
        return "lecturer/edit-mentoring-schedule";
    }

    @PostMapping("/mentoring-history/{id}/edit")
    public String editSchedule(
            @PathVariable Long id,
            @Valid @ModelAttribute EditMentoringScheduleForm editMentoringScheduleForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
            model.addAttribute("timeSlots", mentoringService.getTimeSlots());
            return "lecturer/edit-mentoring-schedule";
        }

        try {
            mentoringService.updateSchedule(loginUser.getId(), id, editMentoringScheduleForm);
            redirectAttributes.addFlashAttribute("success", "Mentoring schedule updated successfully");
            return "redirect:/lecturer/mentoring-history";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
            model.addAttribute("timeSlots", mentoringService.getTimeSlots());
            model.addAttribute("error", ex.getMessage());
            return "lecturer/edit-mentoring-schedule";
        }
    }

    @PostMapping("/mentoring-history/{id}/complete")
    public String complete(
            @PathVariable Long id,
            @Valid @ModelAttribute CompleteMentoringForm completeMentoringForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
            model.addAttribute("grades", EvaluationGrade.values());
            return "lecturer/complete-mentoring";
        }

        try {
            mentoringService.complete(loginUser.getId(), id, completeMentoringForm);
            redirectAttributes.addFlashAttribute("success", "Mentoring session completed successfully");
            return "redirect:/lecturer/mentoring-history";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("sessionView", mentoringService.findLecturerBooking(loginUser.getId(), id));
            model.addAttribute("grades", EvaluationGrade.values());
            model.addAttribute("error", ex.getMessage());
            return "lecturer/complete-mentoring";
        }
    }

    @PostMapping("/mentoring-history/{id}/accept")
    public String accept(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
            mentoringService.accept(loginUser.getId(), id);
            redirectAttributes.addFlashAttribute("success", "Mentoring session accepted successfully");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/lecturer/mentoring-history";
    }
}
