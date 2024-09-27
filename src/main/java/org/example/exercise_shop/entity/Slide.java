package org.example.exercise_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "slide")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String imageUrl;

    String title;

    @Column(length = 500)
    String description;

    String link;

    int orderIndex;

    boolean active;
    @ManyToOne
    @JoinColumn(name = "slider_id")
    Slider slider;
}