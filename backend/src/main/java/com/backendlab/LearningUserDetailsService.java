package com.backendlab;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LearningUserDetailsService implements UserDetailsService {
    private final LearningAccountRepository accounts;

    public LearningUserDetailsService(LearningAccountRepository accounts) { this.accounts = accounts; }

    @Override
    public UserDetails loadUserByUsername(String email) {
        LearningAccount account = accounts.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản."));
        return new LearningPrincipal(account.getId(), account.getEmail(), account.getPasswordHash());
    }
}
