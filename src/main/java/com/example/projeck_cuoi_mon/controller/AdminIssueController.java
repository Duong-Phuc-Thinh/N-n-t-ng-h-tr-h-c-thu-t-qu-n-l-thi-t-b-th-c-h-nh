package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.IssueBorrowingForm;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.service.EquipmentIssueService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/issues")
public class AdminIssueController {

    private final EquipmentIssueService equipmentIssueService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("records", equipmentIssueService.findWaitingAllocationRecords());
        model.addAttribute("issueBorrowingForm", new IssueBorrowingForm());
        return "admin/issues/list";
    }

    @PostMapping("/confirm")
    public String confirmIssue(
            @Valid @ModelAttribute IssueBorrowingForm issueBorrowingForm,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu mã bản ghi mượn thiết bị");
            return "redirect:/admin/issues";
        }

        try {
            SessionUser admin = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
            equipmentIssueService.confirmIssue(issueBorrowingForm.getBorrowingRecordId(), admin.getId());
            redirectAttributes.addFlashAttribute("success", "Cấp phát thiết bị thành công");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/issues";
    }
}
