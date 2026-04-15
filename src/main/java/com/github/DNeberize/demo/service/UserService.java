package com.github.DNeberize.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.DNeberize.demo.domain.AppUser;
import com.github.DNeberize.demo.repository.AppUserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    public static final String SESSION_USER_ID = "currentUserId";

    private final AppUserRepository appUserRepository;
    private final WordNormalizer wordNormalizer;

    public UserService(AppUserRepository appUserRepository, WordNormalizer wordNormalizer) {
        this.appUserRepository = appUserRepository;
        this.wordNormalizer = wordNormalizer;
    }

    @Transactional
    public AppUser signIn(String rawUsername, HttpSession session) {
        String username = wordNormalizer.normalizeUsername(rawUsername);
        AppUser user = appUserRepository.findByUsername(username)
                .orElseGet(() -> appUserRepository.save(new AppUser(username)));
        session.setAttribute(SESSION_USER_ID, user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> getCurrentUser(HttpSession session) {
        Object value = session.getAttribute(SESSION_USER_ID);
        if (!(value instanceof Long userId)) {
            return Optional.empty();
        }
        return appUserRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public List<AppUser> getLeaderboard() {
        return appUserRepository.findAll(PageRequest.of(0, 12,
            Sort.by(Sort.Order.desc("wins"), Sort.Order.desc("bestStreak"),
                Sort.Order.desc("gamesPlayed"), Sort.Order.asc("username"))))
            .getContent();
    }

    public void signOut(HttpSession session) {
        session.removeAttribute(SESSION_USER_ID);
    }
}