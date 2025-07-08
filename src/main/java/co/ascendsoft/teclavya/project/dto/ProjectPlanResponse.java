package co.ascendsoft.teclavya.project.dto;

import java.util.List;

public record ProjectPlanResponse(
        String title,
        String problemStatement,
        List<String> features,
        List<String> suggestedTechStack,
        String folderStructure,
        List<String> dependencies,
        List<String> tutorials,
        List<String> prerequisites,
        List<RoadmapPhase> roadmap
) {
}
