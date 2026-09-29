package com.mukminov.controller.ui;

import com.mukminov.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Hidden
@Controller
@RequiredArgsConstructor
public class NetworkingUiController {

    @GetMapping("/board")
    public String getBoardPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("currentUserId", userDetails.getUser().getId());

        boolean isMentor = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("MENTOR"));
        model.addAttribute("isMentor", isMentor);

        return "board";
    }

    @GetMapping("/notifications")
    public String getNotificationsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("currentUserId", userDetails.getUser().getId());
        return "notifications";
    }
}