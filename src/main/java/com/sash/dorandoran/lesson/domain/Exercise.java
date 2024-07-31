package com.sash.dorandoran.lesson.domain;

import com.sash.dorandoran.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Exercise extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String exerciseText;
    private String answerText;
    private String answerAudioUrl;
    private String feedback;
    private int score;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grading> grading = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Builder
    public Exercise(String exerciseText, String answerText, String answerAudioUrl, String feedback, int score, Lesson lesson, List<Grading> grading) {
        this.exerciseText = exerciseText;
        this.answerText = answerText;
        this.answerAudioUrl = answerAudioUrl;
        this.feedback = feedback;
        this.score = score;
        this.lesson = lesson;
        this.grading = grading;
    }

    public void updateAnswerAudioUrl(String audioUrl) {
        this.answerAudioUrl = audioUrl;
    }

    public void updateFeedbackInfo(String answerText, String feedback, int score) {
        this.answerText = answerText;
        this.feedback = feedback;
        this.score = score;
    }

    public void addGrading(Grading grading) {
        this.grading.add(grading);
        grading.setExercise(this);
    }
}