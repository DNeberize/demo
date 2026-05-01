package com.github.DNeberize.demo.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.DNeberize.demo.service.UserService;
import com.github.DNeberize.demo.service.WordleGameService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GamePageController {

    private final UserService userService;
    private final WordleGameService wordleGameService;
    private final String heroTitle;
    private final String heroCopy;

    public GamePageController(UserService userService, WordleGameService wordleGameService,
            @Value("${app.ui.hero-title}") String heroTitle,
            @Value("${app.ui.hero-copy}") String heroCopy) {
        this.userService = userService;
        this.wordleGameService = wordleGameService;
        this.heroTitle = heroTitle;
        this.heroCopy = heroCopy;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("leaderboard", userService.getLeaderboard());
        model.addAttribute("dictionarySize", wordleGameService.getDictionarySize());
        model.addAttribute("heroTitle", heroTitle);
        model.addAttribute("heroCopy", heroCopy);
        userService.getCurrentUser(session).ifPresent(currentUser -> {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("homeView", wordleGameService.buildHomeView(currentUser.getId()));
        });
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        model.addAttribute("leaderboard", userService.getLeaderboard());
        userService.getCurrentUser(session).ifPresent(currentUser -> model.addAttribute("currentUser", currentUser));
        return "about";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, HttpSession session) {
        model.addAttribute("leaderboard", userService.getLeaderboard());
        userService.getCurrentUser(session).ifPresent(currentUser -> model.addAttribute("currentUser", currentUser));
        return "leaderboard";
    }

    @PostMapping("/game/new")
    public String newGame(HttpSession session, RedirectAttributes redirectAttributes) {
        return userService.getCurrentUser(session)
                .map(currentUser -> {
                    try {
                        wordleGameService.startNewGame(currentUser.getId());
                        redirectAttributes.addFlashAttribute("bannerMessage", "New round started.");
                    }
                    catch (IllegalStateException ex) {
                        redirectAttributes.addFlashAttribute("bannerError", ex.getMessage());
                    }
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("bannerError", "Choose a player name first.");
                    return "redirect:/";
                });
    }
}