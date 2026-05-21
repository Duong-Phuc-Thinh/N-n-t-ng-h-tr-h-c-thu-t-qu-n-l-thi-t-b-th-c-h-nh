package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.LecturerEquipmentRequestForm;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.service.LecturerEquipmentRequestService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lecturer/equipment-requests")
public class LecturerEquipmentRequestController {

    private final LecturerEquipmentRequestService lecturerEquipmentRequestService;

    @GetMapping({"", "/"})
    public String history(HttpSession session, Model model) {
        SessionUser lecturer = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("requests", lecturerEquipmentRequestService.findMyRequests(lecturer.getId()));
        return "lecturer/equipment-request-history";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("requestForm", new LecturerEquipmentRequestForm());
        addFormData(model);
        return "lecturer/equipment-request-form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("requestForm") LecturerEquipmentRequestForm requestForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addFormData(model);
            return "lecturer/equipment-request-form";
        }

        try {
            SessionUser lecturer = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
            lecturerEquipmentRequestService.createRequest(lecturer.getId(), requestForm);
            redirectAttributes.addFlashAttribute("success", "Equipment request sent to admin successfully");
            return "redirect:/lecturer/equipment-requests";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            addFormData(model);
            return "lecturer/equipment-request-form";
        }
    }

    private void addFormData(Model model) {
        model.addAttribute("equipments", lecturerEquipmentRequestService.findRequestableEquipments());
        model.addAttribute("today", LocalDate.now());
    }
}
