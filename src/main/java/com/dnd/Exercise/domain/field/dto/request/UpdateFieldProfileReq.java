package com.dnd.Exercise.domain.field.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UpdateFieldProfileReq {
    @NotBlank
    private String name;

    private MultipartFile profileImg;

    private String rule;

    private String description;
}
