package com.dnd.Exercise.domain.userField.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopPlayerDto {

    private String name;

    private int value;

    public TopPlayerDto(){
        this.name = null;
        this.value = 0;
    }

    public void updateIfLarger(Long newValue, String newName) {
        if (this.value < newValue) {
            this.value = Math.toIntExact(newValue);
            this.name = newName;
        }
    }
}
