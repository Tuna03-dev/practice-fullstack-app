package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {
    String name;
    String phone;
    String province;
    String district;
    String ward;
    String street;
}
