package com.dnd.Exercise.domain.exercise.repository;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, ExerciseRepositoryCustom {
    List<Exercise> findAllByExerciseDateAndUserIdIn(LocalDate exerciseDate, List<Long> userIds);

    @EntityGraph(attributePaths = "user")
    Optional<Exercise> findWithUserById(Long exerciseId);

    @Query("select e from Exercise e "
            + "where e.exerciseDate = :exerciseDate and e.user.id = :userId "
            + "order by e.recordingDateTime desc")
    List<Exercise> findAllExercisesByDateAndUser(@Param("exerciseDate") LocalDate exerciseDate, @Param("userId") Long userId);

    Optional<Exercise> findById(long id);

    Optional<Exercise> findByAppleUidAndUserId(String appleUid, Long userId);
}
