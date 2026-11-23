package com.backendlab;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Pattern GMAIL = Pattern.compile("^[A-Z0-9._%+-]+@gmail\\.com$", Pattern.CASE_INSENSITIVE);
    private final LearningAccountRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(LearningAccountRepository accounts, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) { return Map.of("token", token.getToken(), "headerName", token.getHeaderName()); }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Credentials credentials,
                                                         HttpServletRequest request, HttpServletResponse response) {
        String email = normalizeEmail(credentials.email());
        if (credentials.password() == null || credentials.password().length() < 8
                || credentials.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu cần có ít nhất 8 ký tự và tối đa 72 byte UTF-8.");
        }
        if (accounts.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gmail này đã được đăng ký.");
        }
        accounts.save(new LearningAccount(email, passwordEncoder.encode(credentials.password())));
        return authenticate(email, credentials.password(), request, response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Credentials credentials,
                                                      HttpServletRequest request, HttpServletResponse response) {
        String email = normalizeEmail(credentials.email());
        return authenticate(email, credentials.password(), request, response);
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        LearningPrincipal principal = (LearningPrincipal) authentication.getPrincipal();
        return Map.of("email", principal.email());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> authenticate(String email, String password,
                                                               HttpServletRequest request, HttpServletResponse response) {
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập mật khẩu.");
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Gmail hoặc mật khẩu chưa đúng.");
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        if (request.getSession(false) == null) request.getSession(true);
        else request.changeSessionId();
        securityContextRepository.saveContext(context, request, response);
        return ResponseEntity.ok(Map.of("email", email));
    }

    private String normalizeEmail(String rawEmail) {
        String email = rawEmail == null ? "" : rawEmail.trim().toLowerCase(Locale.ROOT);
        if (!GMAIL.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập địa chỉ Gmail hợp lệ.");
        }
        return email;
    }

    public record Credentials(String email, String password) { }
}
