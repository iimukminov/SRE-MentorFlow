package com.mukminov.controller.ui;

import com.mukminov.model.enums.RoleType;
import com.mukminov.security.CustomUserDetails;
import com.mukminov.service.core.ConsultationService;
import com.mukminov.service.core.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Hidden
public class ConsultationUiController {

    private final ConsultationService consultationService;
    private final UserService userService;

    @GetMapping("/consultations")
    public String getConsultationsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUser().getId();

        model.addAttribute("currentUserId", userId);
        boolean isMentor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_MENTOR.name()));
        model.addAttribute("isMentor", isMentor);

        model.addAttribute("consultations", consultationService.getConsultations(
                isMentor ? userId : null,
                !isMentor ? userId : null
        ));

        if (isMentor) {
            model.addAttribute("mentees", userService.getMentees(userId));
        }

        return "consultations_view";
    }
}