package com.github.DNeberize.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.DNeberize.demo.service.UserService;
import com.github.DNeberize.demo.web.dto.LoginForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionController {

    private final UserService userService;

    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/session/user")
    public String signIn(@ModelAttribute LoginForm loginForm, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            userService.signIn(loginForm.getUsername(), session);
            redirectAttributes.addFlashAttribute("bannerMessage", "Player profile is ready.");
        }
        catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("bannerError", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/session/logout")
    public String signOut(HttpSession session, RedirectAttributes redirectAttributes) {
        userService.signOut(session);
        redirectAttributes.addFlashAttribute("bannerMessage", "Player signed out.");
        return "redirect:/";
    }
}