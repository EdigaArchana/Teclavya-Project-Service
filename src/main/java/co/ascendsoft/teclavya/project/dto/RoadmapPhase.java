package co.ascendsoft.teclavya.project.dto;

import java.util.List;

public record RoadmapPhase(
        String phase,
        String description,
        String estimatedTime,
        List<String> resources
) {
}
