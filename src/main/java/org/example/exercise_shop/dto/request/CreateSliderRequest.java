package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.SliderName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSliderRequest {
    SliderName name;
    boolean active;
}
