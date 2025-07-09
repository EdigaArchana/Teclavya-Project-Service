package co.ascendsoft.teclavya.project.dto;

import java.util.List;

public record ProjectPreferenceRequest(
        String userId,
        String domain,
        String difficulty,
        String projectType,
        List<String> techStack,
        String timeEstimate
) {
}
