package com.backendlab;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LearningController {
    private final ProgressRepository repository;
    public LearningController(ProgressRepository repository) { this.repository = repository; }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "ok", "service", "backend-learning-lab"); }

    @GetMapping("/progress/me")
    public List<Progress> progress(@AuthenticationPrincipal LearningPrincipal principal) {
        return repository.findByLearnerId(principal.id().toString());
    }

    @PutMapping("/progress/me/{lessonId}")
    public ResponseEntity<Progress> complete(@AuthenticationPrincipal LearningPrincipal principal,
                                              @PathVariable String lessonId, @RequestBody Map<String, Boolean> body) {
        String learnerId = principal.id().toString();
        Progress item = repository.findByLearnerIdAndLessonId(learnerId, lessonId)
                .orElseGet(() -> new Progress(learnerId, lessonId, false));
        item.setCompleted(Boolean.TRUE.equals(body.get("completed")));
        return ResponseEntity.ok(repository.save(item));
    }

    @PostMapping("/progress/me/import")
    public List<Progress> importLocalProgress(@AuthenticationPrincipal LearningPrincipal principal,
                                               @RequestBody Map<String, List<String>> body) {
        String learnerId = principal.id().toString();
        if (repository.existsByLearnerId(learnerId)) return repository.findByLearnerId(learnerId);
        List<String> completed = body.getOrDefault("completed", List.of()).stream()
                .filter(id -> id != null && id.matches("[a-zA-Z0-9_-]{1,120}"))
                .distinct().toList();
        return repository.saveAll(completed.stream().map(id -> new Progress(learnerId, id, true)).toList());
    }

    @DeleteMapping("/progress/me")
    public ResponseEntity<Void> resetProgress(@AuthenticationPrincipal LearningPrincipal principal) {
        repository.deleteByLearnerId(principal.id().toString());
        return ResponseEntity.noContent().build();
    }
}
