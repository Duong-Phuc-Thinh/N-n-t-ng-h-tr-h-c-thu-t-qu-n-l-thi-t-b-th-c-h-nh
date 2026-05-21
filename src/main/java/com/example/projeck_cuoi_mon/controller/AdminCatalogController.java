package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.dto.request.DepartmentForm;
import com.example.projeck_cuoi_mon.dto.request.LabRoomTypeForm;
import com.example.projeck_cuoi_mon.dto.request.MajorForm;
import com.example.projeck_cuoi_mon.dto.response.LabRoomTypeView;
import com.example.projeck_cuoi_mon.service.AdminCatalogService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/catalog")
public class AdminCatalogController {

    private final AdminCatalogService adminCatalogService;

    @GetMapping("/departments")
    public String departments(Model model) {
        populateDepartmentPage(model);
        if (!model.containsAttribute("departmentForm")) {
            model.addAttribute("departmentForm", new DepartmentForm());
        }
        if (!model.containsAttribute("majorForm")) {
            model.addAttribute("majorForm", new MajorForm());
        }
        model.addAttribute("departmentFormAction", "/admin/catalog/departments");
        model.addAttribute("majorFormAction", "/admin/catalog/majors");
        return "admin/catalog/departments";
    }

    @PostMapping("/departments")
    public String createDepartment(
            @Valid @ModelAttribute DepartmentForm departmentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateDepartmentPage(model);
            model.addAttribute("majorForm", new MajorForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            return "admin/catalog/departments";
        }

        try {
            adminCatalogService.createDepartment(departmentForm);
            redirectAttributes.addFlashAttribute("success", "Da them khoa moi");
            return "redirect:/admin/catalog/departments";
        } catch (IllegalArgumentException ex) {
            populateDepartmentPage(model);
            model.addAttribute("majorForm", new MajorForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/departments";
        }
    }

    @GetMapping("/departments/{id}/edit")
    public String editDepartment(@PathVariable Long id, Model model) {
        populateDepartmentPage(model);
        model.addAttribute("departmentForm", adminCatalogService.getDepartmentForm(id));
        model.addAttribute("majorForm", new MajorForm());
        model.addAttribute("departmentFormAction", "/admin/catalog/departments/" + id);
        model.addAttribute("majorFormAction", "/admin/catalog/majors");
        return "admin/catalog/departments";
    }

    @PostMapping("/departments/{id}")
    public String updateDepartment(
            @PathVariable Long id,
            @Valid @ModelAttribute DepartmentForm departmentForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateDepartmentPage(model);
            model.addAttribute("majorForm", new MajorForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments/" + id);
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            return "admin/catalog/departments";
        }

        try {
            adminCatalogService.updateDepartment(id, departmentForm);
            redirectAttributes.addFlashAttribute("success", "Da cap nhat khoa");
            return "redirect:/admin/catalog/departments";
        } catch (IllegalArgumentException ex) {
            populateDepartmentPage(model);
            model.addAttribute("majorForm", new MajorForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments/" + id);
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/departments";
        }
    }

    @PostMapping("/departments/{id}/disable")
    public String disableDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminCatalogService.disableDepartment(id);
        redirectAttributes.addFlashAttribute("success", "Da ngung hoat dong khoa");
        return "redirect:/admin/catalog/departments";
    }

    @PostMapping("/majors")
    public String createMajor(
            @Valid @ModelAttribute MajorForm majorForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateDepartmentPage(model);
            model.addAttribute("departmentForm", new DepartmentForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            return "admin/catalog/departments";
        }

        try {
            adminCatalogService.createMajor(majorForm);
            redirectAttributes.addFlashAttribute("success", "Da them nganh dao tao");
            return "redirect:/admin/catalog/departments";
        } catch (IllegalArgumentException ex) {
            populateDepartmentPage(model);
            model.addAttribute("departmentForm", new DepartmentForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors");
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/departments";
        }
    }

    @GetMapping("/majors/{id}/edit")
    public String editMajor(@PathVariable Long id, Model model) {
        populateDepartmentPage(model);
        model.addAttribute("departmentForm", new DepartmentForm());
        model.addAttribute("majorForm", adminCatalogService.getMajorForm(id));
        model.addAttribute("departmentFormAction", "/admin/catalog/departments");
        model.addAttribute("majorFormAction", "/admin/catalog/majors/" + id);
        return "admin/catalog/departments";
    }

    @PostMapping("/majors/{id}")
    public String updateMajor(
            @PathVariable Long id,
            @Valid @ModelAttribute MajorForm majorForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateDepartmentPage(model);
            model.addAttribute("departmentForm", new DepartmentForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors/" + id);
            return "admin/catalog/departments";
        }

        try {
            adminCatalogService.updateMajor(id, majorForm);
            redirectAttributes.addFlashAttribute("success", "Da cap nhat nganh dao tao");
            return "redirect:/admin/catalog/departments";
        } catch (IllegalArgumentException ex) {
            populateDepartmentPage(model);
            model.addAttribute("departmentForm", new DepartmentForm());
            model.addAttribute("departmentFormAction", "/admin/catalog/departments");
            model.addAttribute("majorFormAction", "/admin/catalog/majors/" + id);
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/departments";
        }
    }

    @PostMapping("/majors/{id}/disable")
    public String disableMajor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminCatalogService.disableMajor(id);
        redirectAttributes.addFlashAttribute("success", "Da ngung hoat dong nganh");
        return "redirect:/admin/catalog/departments";
    }

    @GetMapping("/lab-room-types")
    public String labRoomTypes(Model model) {
        populateLabRoomTypePage(model);
        if (!model.containsAttribute("labRoomTypeForm")) {
            model.addAttribute("labRoomTypeForm", new LabRoomTypeForm());
        }
        model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types");
        return "admin/catalog/lab-room-types";
    }

    @PostMapping("/lab-room-types")
    public String createLabRoomType(
            @Valid @ModelAttribute LabRoomTypeForm labRoomTypeForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateLabRoomTypePage(model);
            model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types");
            return "admin/catalog/lab-room-types";
        }

        try {
            adminCatalogService.createLabRoomType(labRoomTypeForm);
            redirectAttributes.addFlashAttribute("success", "Da them danh muc phong lab");
            return "redirect:/admin/catalog/lab-room-types";
        } catch (IllegalArgumentException ex) {
            populateLabRoomTypePage(model);
            model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types");
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/lab-room-types";
        }
    }

    @GetMapping("/lab-room-types/{id}/edit")
    public String editLabRoomType(@PathVariable Long id, Model model) {
        populateLabRoomTypePage(model);
        model.addAttribute("labRoomTypeForm", adminCatalogService.getLabRoomTypeForm(id));
        model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types/" + id);
        return "admin/catalog/lab-room-types";
    }

    @PostMapping("/lab-room-types/{id}")
    public String updateLabRoomType(
            @PathVariable Long id,
            @Valid @ModelAttribute LabRoomTypeForm labRoomTypeForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            populateLabRoomTypePage(model);
            model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types/" + id);
            return "admin/catalog/lab-room-types";
        }

        try {
            adminCatalogService.updateLabRoomType(id, labRoomTypeForm);
            redirectAttributes.addFlashAttribute("success", "Da cap nhat danh muc phong lab");
            return "redirect:/admin/catalog/lab-room-types";
        } catch (IllegalArgumentException ex) {
            populateLabRoomTypePage(model);
            model.addAttribute("labRoomTypeFormAction", "/admin/catalog/lab-room-types/" + id);
            model.addAttribute("error", ex.getMessage());
            return "admin/catalog/lab-room-types";
        }
    }

    @PostMapping("/lab-room-types/{id}/disable")
    public String disableLabRoomType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminCatalogService.disableLabRoomType(id);
        redirectAttributes.addFlashAttribute("success", "Da ngung hoat dong danh muc phong lab");
        return "redirect:/admin/catalog/lab-room-types";
    }

    private void populateDepartmentPage(Model model) {
        model.addAttribute("departments", adminCatalogService.findDepartmentViews());
        model.addAttribute("departmentOptions", adminCatalogService.findAllDepartments());
        model.addAttribute("majors", adminCatalogService.findAllMajorViews());
        model.addAttribute("stats", adminCatalogService.getStats());
    }

    private void populateLabRoomTypePage(Model model) {
        List<LabRoomTypeView> labRoomTypes = adminCatalogService.findLabRoomTypeViews();
        model.addAttribute("labRoomTypes", labRoomTypes);
        model.addAttribute("totalLabRoomTypes", labRoomTypes.size());
        model.addAttribute("activeLabRoomTypes", labRoomTypes.stream().filter(LabRoomTypeView::active).count());
        model.addAttribute("maintenanceEquipments", labRoomTypes.stream().mapToLong(LabRoomTypeView::maintenanceEquipmentCount).sum());
        model.addAttribute("totalLabEquipments", labRoomTypes.stream().mapToLong(LabRoomTypeView::equipmentCount).sum());
    }
}
