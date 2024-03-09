package com.dnd.Exercise.domain.field.entity;

import static com.dnd.Exercise.domain.field.dto.response.FieldRole.GUEST;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.LEADER;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.MEMBER;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.RECRUITING;
import static com.dnd.Exercise.domain.field.entity.enums.Period.ONE_WEEK;
import static com.dnd.Exercise.domain.field.entity.enums.Period.THREE_WEEKS;
import static com.dnd.Exercise.domain.field.entity.enums.Period.TWO_WEEKS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_COMPLETED;
import static com.dnd.Exercise.global.error.dto.ErrorCode.OPPONENT_NOT_FOUND;
import static com.dnd.Exercise.global.error.dto.ErrorCode.RECRUITING_YET;
import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.domain.field.dto.response.FieldRole;
import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.BaseEntity;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Field extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    private String name;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Strength strength;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private String rule;

    private int maxSize;

    private int currentSize;

    @Enumerated(EnumType.STRING)
    private Period period;

    private String description;

    private Long leaderId;

    @Enumerated(EnumType.STRING)
    private FieldStatus fieldStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "opponent_id")
    private Field opponent;

    @Builder
    public Field(String name, String profileImg, Strength strength, Goal goal,
            String rule, int maxSize, Period period,
            String description, Long leaderId, FieldType fieldType,
            SkillLevel skillLevel) {
        this.name = name;
        this.profileImg = profileImg;
        this.strength = strength;
        this.goal = goal;
        this.rule = rule;
        this.maxSize = maxSize;
        this.currentSize = 1;
        this.period = period;
        this.description = description;
        this.leaderId = leaderId;
        this.fieldStatus = FieldStatus.RECRUITING;
        this.startDate = null;
        this.endDate = null;
        this.fieldType = fieldType;
        this.skillLevel = skillLevel;
        this.opponent = null;
    }

    public void changeOpponent(Field field){
        this.opponent = field;
        field.opponent = this;
    }

    public void addMember(){
        this.currentSize += 1;
    }

    public void subtractMember() {
        this.currentSize -= 1;
    }

    public void subtractMember(int cnt){
        this.currentSize -= cnt;
    }

    public void changeProfileImg(String imgUrl){
        this.profileImg = imgUrl;
    }

    public void changeLeader(Long leaderId){
        this.leaderId = leaderId;
    }

    public void updateDate(Period period){
        long plusDays = 0;
        if (ONE_WEEK.equals(period)){
            plusDays = 7;
        } else if (TWO_WEEKS.equals(period)) {
            plusDays = 14;
        } else if(THREE_WEEKS.equals(period)){
            plusDays = 21;
        }
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(plusDays);
    }

    public void validateNotRecruiting() {
        if (RECRUITING.equals(this.fieldStatus)) {
            throw new BusinessException(RECRUITING_YET);
        }
    }

    public void validateCompleted() {
        if(!COMPLETED.equals(this.fieldStatus)){
            throw new BusinessException(NOT_COMPLETED);
        }
    }

    public FieldRole determineFieldRole(User user, Boolean isMember) {
        Long userId = user.getId();
        if (userId.equals(this.leaderId)) return LEADER;
        else if (isMember) return MEMBER;
        else return GUEST;
    }

    public int calculateFieldDifference(Field field) {
        return Math.abs(this.skillLevel.ordinal() - field.getSkillLevel().ordinal())
                + Math.abs(this.strength.ordinal() - field.getStrength().ordinal())
                + Math.abs(this.maxSize - field.getMaxSize());
    }

    public boolean isNotMatchedField(List<String> matchedFieldIds) {
        return !matchedFieldIds.contains(String.valueOf(this.id));
    }

    public boolean isNotSameField(Field field) {
        return !field.getId().equals(this.id);
    }

    public void validateOpponentPresence() {
        if (this.opponent == null) {
            throw new BusinessException(OPPONENT_NOT_FOUND);
        }
    }

    public void updateFieldStatusForScheduler() {
        if (RECRUITING.equals(this.fieldStatus) && this.opponent != null) {
            this.fieldStatus = IN_PROGRESS;
            updateDate(period);
        } else if (IN_PROGRESS.equals(this.fieldStatus) && LocalDate.now().equals(this.endDate)) {
            this.fieldStatus = COMPLETED;
        }
    }
}
