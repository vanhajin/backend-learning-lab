package com.backendlab;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByLearnerId(String learnerId);
    boolean existsByLearnerId(String learnerId);
    void deleteByLearnerId(String learnerId);
    Optional<Progress> findByLearnerIdAndLessonId(String learnerId, String lessonId);
}
