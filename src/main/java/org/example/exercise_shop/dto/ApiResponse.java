package org.example.exercise_shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class
ApiResponse<T> {
    @Builder.Default
    int code = 200;
    String message;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
    T data;
}
