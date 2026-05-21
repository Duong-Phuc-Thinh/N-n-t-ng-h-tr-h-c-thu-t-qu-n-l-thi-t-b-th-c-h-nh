package com.example.projeck_cuoi_mon.controller;

import com.example.projeck_cuoi_mon.config.SessionConstants;
import com.example.projeck_cuoi_mon.dto.request.EditProfileForm;
import com.example.projeck_cuoi_mon.dto.response.ProfileView;
import com.example.projeck_cuoi_mon.dto.response.SessionUser;
import com.example.projeck_cuoi_mon.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        model.addAttribute("profile", profileService.getProfile(loginUser.getId()));
        model.addAttribute("loginUser", loginUser);
        return "profile/detail";
    }

    @GetMapping("/profile/edit")
    public String editForm(HttpSession session, Model model) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        ProfileView view = profileService.getProfile(loginUser.getId());

        EditProfileForm form = new EditProfileForm();
        form.setFullName(view.getFullName());
        form.setPhone(view.getPhone());
        form.setClassName(view.getClassName());
        form.setAcademicTitle(view.getAcademicTitle());
        form.setSpecialization(view.getSpecialty());

        model.addAttribute("profile", view);
        model.addAttribute("editProfileForm", form);
        model.addAttribute("loginUser", loginUser);
        return "profile/edit";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            @Valid @ModelAttribute EditProfileForm editProfileForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SessionUser loginUser = (SessionUser) session.getAttribute(SessionConstants.LOGIN_USER);
        if (bindingResult.hasErrors()) {
            ProfileView view = profileService.getProfile(loginUser.getId());
            model.addAttribute("profile", view);
            model.addAttribute("loginUser", loginUser);
            return "profile/edit";
        }

        try {
            profileService.updateProfile(loginUser.getId(), editProfileForm);

            // Cập nhật lại session user với tên mới
            SessionUser newLoginUser = new SessionUser(
                    loginUser.getId(),
                    loginUser.getEmail(),
                    editProfileForm.getFullName(),
                    loginUser.getRole()
            );
            session.setAttribute(SessionConstants.LOGIN_USER, newLoginUser);

            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ cá nhân thành công");
            return "redirect:/profile";
        } catch (IllegalArgumentException ex) {
            ProfileView view = profileService.getProfile(loginUser.getId());
            model.addAttribute("profile", view);
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("error", ex.getMessage());
            return "profile/edit";
        }
    }
}
