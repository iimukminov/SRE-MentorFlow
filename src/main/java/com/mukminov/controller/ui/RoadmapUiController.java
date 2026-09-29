package com.mukminov.controller.ui;

import com.mukminov.model.enums.RoleType;
import com.mukminov.security.CustomUserDetails;
import com.mukminov.service.core.RoadmapService;
import com.mukminov.service.core.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
@RequiredArgsConstructor
public class RoadmapUiController {

    private final RoadmapService roadmapService;
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/roadmap";
    }

    @GetMapping("/roadmap")
    public String getRoadmapsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long currentUserId = userDetails.getUser().getId();
        model.addAttribute("currentUserId", currentUserId);

        boolean isMentor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleType.ROLE_MENTOR.name()));
        model.addAttribute("isMentor", isMentor);

        if (isMentor) {
            model.addAttribute("roadmaps", roadmapService.getRoadmaps(currentUserId, null));
            model.addAttribute("mentees", userService.getMentees(currentUserId));
        } else {
            model.addAttribute("roadmaps", roadmapService.getRoadmaps(null, currentUserId));
        }

        return "roadmaps";
    }
}