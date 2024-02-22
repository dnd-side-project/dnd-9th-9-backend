package com.dnd.Exercise.domain.BattleEntry.dto.response;

import com.dnd.Exercise.domain.BattleEntry.entity.BattleEntry;
import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindBattleEntriesDto {

    private Long entryId;

    private Long fieldId;

    private String name;

    private FieldType fieldType;

    private int currentSize;

    private int maxSize;

    private SkillLevel skillLevel;

    private Period period;
    
    public static FindBattleEntriesDto toSentEntryDto(BattleEntry battleEntry){
        Field hostField = battleEntry.getHostField();
        Long entryId = hostField.getId();
        return getFindBattleEntriesDto(hostField, entryId);
    }

    public static FindBattleEntriesDto toSentEntryDto(MemberEntry memberEntry){
        Field hostField = memberEntry.getHostField();
        Long entryId = hostField.getId();
        return getFindBattleEntriesDto(hostField, entryId);
    }

    public static FindBattleEntriesDto toReceivedEntryDto(BattleEntry battleEntry){
        Field entrantField = battleEntry.getEntrantField();
        Long entryId = entrantField.getId();
        return getFindBattleEntriesDto(entrantField, entryId);
    }

    private static FindBattleEntriesDto getFindBattleEntriesDto(Field field, Long entryId) {
        return FindBattleEntriesDto.builder()
                .entryId(entryId)
                .fieldId(field.getId())
                .name(field.getName())
                .fieldType(field.getFieldType())
                .currentSize(field.getCurrentSize())
                .maxSize(field.getMaxSize())
                .skillLevel(field.getSkillLevel())
                .period(field.getPeriod())
                .build();
    }
}
