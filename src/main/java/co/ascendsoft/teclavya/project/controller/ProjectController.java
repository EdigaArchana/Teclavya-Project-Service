package co.ascendsoft.teclavya.project.controller;

import co.ascendsoft.teclavya.project.dto.ProjectPlanResponse;
import co.ascendsoft.teclavya.project.dto.ProjectPreferenceRequest;
import co.ascendsoft.teclavya.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Validated
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectPlanResponse> createProject(
            @RequestBody ProjectPreferenceRequest request
    ) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.OK);
    }


}
