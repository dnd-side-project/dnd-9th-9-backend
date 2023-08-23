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

    List<Exercise> findAllByExerciseDateBetweenAndUserIdIn(LocalDate startDate, LocalDate endDate, List<Long> ids);

    @EntityGraph(attributePaths = "user")
    Optional<Exercise> findWithUserById(Long exerciseId);

    @Query("select e from Exercise e "
            + "where e.exerciseDate = :exerciseDate and e.user.id = :userId "
            + "order by e.recordingDateTime desc")
    List<Exercise> findAllExercisesByDateAndUser(@Param("exerciseDate") LocalDate exerciseDate, @Param("userId") Long userId);

    Optional<Exercise> findById(long id);

    Optional<Exercise> findByAppleUidAndUserId(String appleUid, Long userId);

    @Query("select coalesce(sum(e.burnedCalorie),0) " +
            "from Exercise e " +
            "where e.user.id = :userId and e.exerciseDate = :exerciseDate")
    int sumDailyBurnedCalorieOfUser(LocalDate exerciseDate, long userId);

    @Query("select coalesce(sum(e.durationMinute),0) " +
            "from Exercise e " +
            "where e.user.id = :userId and e.exerciseDate = :exerciseDate")
    int sumDailyDurationMinuteOfUser(LocalDate exerciseDate, long userId);

    int countByExerciseDateAndUserId(LocalDate exerciseDate, long userId);
}
