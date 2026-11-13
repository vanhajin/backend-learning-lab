package com.backendlab;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/progress/{learnerId}")
    public List<Progress> progress(@PathVariable String learnerId) { return repository.findByLearnerId(learnerId); }

    @PutMapping("/progress/{learnerId}/{lessonId}")
    public ResponseEntity<Progress> complete(@PathVariable String learnerId, @PathVariable String lessonId, @RequestBody Map<String, Boolean> body) {
        Progress item = repository.findByLearnerIdAndLessonId(learnerId, lessonId)
                .orElseGet(() -> new Progress(learnerId, lessonId, false));
        item.setCompleted(Boolean.TRUE.equals(body.get("completed")));
        return ResponseEntity.ok(repository.save(item));
    }
}
