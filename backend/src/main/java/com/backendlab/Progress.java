package com.backendlab;

import jakarta.persistence.*;

@Entity
@Table(name = "lesson_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"learner_id", "lesson_id"}))
public class Progress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "learner_id", nullable = false) private String learnerId;
    @Column(name = "lesson_id", nullable = false) private String lessonId;
    private boolean completed;
    protected Progress() { }
    public Progress(String learnerId, String lessonId, boolean completed) { this.learnerId = learnerId; this.lessonId = lessonId; this.completed = completed; }
    public String getLearnerId() { return learnerId; }
    public String getLessonId() { return lessonId; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
