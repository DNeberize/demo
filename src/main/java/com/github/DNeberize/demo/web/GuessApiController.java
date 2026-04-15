package com.github.DNeberize.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.DNeberize.demo.service.UserService;
import com.github.DNeberize.demo.service.WordleGameService;
import com.github.DNeberize.demo.web.dto.ApiErrorResponse;
import com.github.DNeberize.demo.web.dto.GuessRequest;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/game")
public class GuessApiController {

    private final UserService userService;
    private final WordleGameService wordleGameService;

    public GuessApiController(UserService userService, WordleGameService wordleGameService) {
        this.userService = userService;
        this.wordleGameService = wordleGameService;
    }

    @PostMapping("/guess")
    public ResponseEntity<?> submitGuess(@RequestBody(required = false) GuessRequest request, HttpSession session) {
        return userService.getCurrentUser(session)
                .<ResponseEntity<?>>map(currentUser -> {
                    try {
                        String guess = request == null ? null : request.guess();
                        return ResponseEntity.ok(wordleGameService.submitGuess(currentUser.getId(), guess));
                    }
                    catch (IllegalArgumentException ex) {
                        return ResponseEntity.badRequest().body(new ApiErrorResponse(ex.getMessage()));
                    }
                    catch (IllegalStateException ex) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse(ex.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiErrorResponse("Choose a player name first.")));
    }
}