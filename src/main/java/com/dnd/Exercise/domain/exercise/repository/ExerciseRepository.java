package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {
    List<Exercise> findAllByExerciseDateAndUserIdIn(LocalDate exerciseDate, List<Long> userIds);

    @EntityGraph(attributePaths = "user")
    Optional<Exercise> findWithUserById(Long exerciseId);
}
