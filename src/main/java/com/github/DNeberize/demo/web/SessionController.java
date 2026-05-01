package com.github.DNeberize.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.DNeberize.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    private final UserService userService;

    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/session/user")
    public String signIn(@RequestParam String username, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            userService.signIn(username, session);
            log.info("User signed in with username={}", username);
            redirectAttributes.addFlashAttribute("bannerMessage", "Player profile is ready.");
        }
        catch (IllegalArgumentException ex) {
            log.warn("Sign in rejected for username={}: {}", username, ex.getMessage());
            redirectAttributes.addFlashAttribute("bannerError", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/session/logout")
    public String signOut(HttpSession session, RedirectAttributes redirectAttributes) {
        userService.signOut(session);
        log.info("Current user signed out");
        redirectAttributes.addFlashAttribute("bannerMessage", "Player signed out.");
        return "redirect:/";
    }
}