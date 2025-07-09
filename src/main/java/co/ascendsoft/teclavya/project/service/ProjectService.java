package co.ascendsoft.teclavya.project.service;

import co.ascendsoft.teclavya.project.dto.ProjectPlanResponse;
import co.ascendsoft.teclavya.project.dto.ProjectPreferenceRequest;

public interface ProjectService {
    ProjectPlanResponse createProject(ProjectPreferenceRequest request);
}
