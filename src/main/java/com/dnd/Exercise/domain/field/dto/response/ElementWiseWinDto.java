package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import java.util.List;
import lombok.Data;

@Data
public class ElementWiseWinDto {
    private WinStatus recordCount;
    private WinStatus goalAchievedCount;
    private WinStatus burnedCalorie;
    private WinStatus exerciseTimeMinute;


    public ElementWiseWinDto(List<WinStatus> elementWiseWinList){
        this.recordCount = elementWiseWinList.get(0);
        this.goalAchievedCount =  elementWiseWinList.get(1);
        this.burnedCalorie =  elementWiseWinList.get(2);
        this.exerciseTimeMinute =  elementWiseWinList.get(3);
    }
}
