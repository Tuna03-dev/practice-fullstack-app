package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    Sender sender;
    List<Recipient> to;
    String htmlContent;
    String subject;
}
