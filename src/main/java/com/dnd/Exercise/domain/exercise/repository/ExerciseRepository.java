package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByExerciseDateAndUserIdIn(LocalDate exerciseDate, List<Long> userIds);
}
