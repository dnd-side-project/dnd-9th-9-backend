package com.dnd.Exercise.domain.userField.dto.response;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.userField.dto.AccumulatedActivityDto;
import com.dnd.Exercise.domain.userField.dto.AccumulatedExerciseDto;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindMyTeamStatusRes {

    private String teamName;
    private Long fieldId;
    private Long daysLeft;
    private TopPlayerDto burnedCalorie;
    private TopPlayerDto recordCount;
    private TopPlayerDto goalAchievedCount;
    private TopPlayerDto exerciseTimeMinute;

    public static FindMyTeamStatusRes from(List<AccumulatedActivityDto> activityValues,
            List<AccumulatedExerciseDto> exerciseValues,
            Field myField) {

        FindMyTeamStatusRes result = new FindMyTeamStatusRes();
        result.initializeTopPlayers();
        result.updateTopPlayersBasedOnActivities(activityValues);
        result.updateTopPlayersBasedOnExercises(exerciseValues);
        result.populateFieldDetails(myField);
        return result;
    }

    private void initializeTopPlayers() {
        this.burnedCalorie = new TopPlayerDto();
        this.goalAchievedCount = new TopPlayerDto();
        this.recordCount = new TopPlayerDto();
        this.exerciseTimeMinute = new TopPlayerDto();
    }

    private void updateTopPlayersBasedOnActivities(List<AccumulatedActivityDto> activities) {
        for (AccumulatedActivityDto activity : activities) {
            this.burnedCalorie.updateIfLarger(activity.getTotalBurnedCalorie(), activity.getName());
            this.goalAchievedCount.updateIfLarger(activity.getGoalAchievedCount(), activity.getName());
        }
    }

    private void updateTopPlayersBasedOnExercises(List<AccumulatedExerciseDto> exercises) {
        for (AccumulatedExerciseDto exercise : exercises) {
            this.recordCount.updateIfLarger(exercise.getExerciseCount(), exercise.getName());
            this.exerciseTimeMinute.updateIfLarger(exercise.getTotalDurationTime(), exercise.getName());
        }
    }

    private void populateFieldDetails(Field myField) {
        this.fieldId = myField.getId();
        this.teamName = myField.getName();
        this.daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), myField.getEndDate());
    }
}
