package com.dnd.Exercise.domain.field.dto.response;

import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ElementWiseWin {
    private WinStatus recordCount;
    private WinStatus goalAchievedCount;
    private WinStatus burnedCalorie;
    private WinStatus exerciseTimeMinute;


    public static ElementWiseWin from(List<WinStatus> elementWiseWinList){
        return ElementWiseWin.builder()
                .recordCount(elementWiseWinList.get(0))
                .goalAchievedCount(elementWiseWinList.get(1))
                .burnedCalorie(elementWiseWinList.get(2))
                .exerciseTimeMinute(elementWiseWinList.get(3))
                .build();
    }
}
