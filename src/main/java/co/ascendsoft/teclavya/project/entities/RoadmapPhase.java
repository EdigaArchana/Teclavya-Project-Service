package co.ascendsoft.teclavya.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phase;
    private String description;
    private String estimatedTime;

    @ElementCollection
    private List<String> resources;
}
