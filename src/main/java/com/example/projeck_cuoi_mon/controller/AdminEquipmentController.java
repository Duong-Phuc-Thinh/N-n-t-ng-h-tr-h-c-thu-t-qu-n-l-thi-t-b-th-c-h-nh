package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.dto.request.EquipmentForm;
import com.example.projeck_cuoi_mon.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/equipments")
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public String list(@RequestParam(required = false) Long labRoomTypeId, Model model) {
        model.addAttribute("equipments", equipmentService.findAll(labRoomTypeId));
        model.addAttribute("stats", equipmentService.getDashboardStats());
        model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
        model.addAttribute("selectedLabRoomTypeId", labRoomTypeId);
        return "admin/equipments/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("equipmentForm", new EquipmentForm());
        model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
        model.addAttribute("formTitle", "Thêm thiết bị mới");
        model.addAttribute("formAction", "/admin/equipments");
        return "admin/equipments/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute EquipmentForm equipmentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
            model.addAttribute("formTitle", "Thêm thiết bị mới");
            model.addAttribute("formAction", "/admin/equipments");
            return "admin/equipments/form";
        }

        try {
            equipmentService.create(equipmentForm);
            redirectAttributes.addFlashAttribute("success", "Equipment created successfully");
            return "redirect:/admin/equipments";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
            model.addAttribute("formTitle", "Thêm thiết bị mới");
            model.addAttribute("formAction", "/admin/equipments");
            return "admin/equipments/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("equipmentForm", equipmentService.getFormForEdit(id));
        model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
        model.addAttribute("formTitle", "Sửa thiết bị");
        model.addAttribute("formAction", "/admin/equipments/" + id);
        return "admin/equipments/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute EquipmentForm equipmentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
            model.addAttribute("formTitle", "Sửa thiết bị");
            model.addAttribute("formAction", "/admin/equipments/" + id);
            return "admin/equipments/form";
        }

        try {
            equipmentService.update(id, equipmentForm);
            redirectAttributes.addFlashAttribute("success", "Equipment updated successfully");
            return "redirect:/admin/equipments";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("labRoomTypes", equipmentService.findAllLabRoomTypes());
            model.addAttribute("formTitle", "Sửa thiết bị");
            model.addAttribute("formAction", "/admin/equipments/" + id);
            return "admin/equipments/form";
        }
    }

    @PostMapping("/{id}/disable")
    public String disable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        equipmentService.disable(id);
        redirectAttributes.addFlashAttribute("success", "Equipment disabled successfully");
        return "redirect:/admin/equipments";
    }
}
