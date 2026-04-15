package com.github.DNeberize.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.DNeberize.demo.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findTop12ByOrderByWinsDescBestStreakDescGamesPlayedDescUsernameAsc();
}