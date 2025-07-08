package co.ascendsoft.teclavya.project.service.impl;

import co.ascendsoft.teclavya.exceptions.models.BadRequestException;
import co.ascendsoft.teclavya.project.dto.ProjectPlanResponse;
import co.ascendsoft.teclavya.project.dto.ProjectPreferenceRequest;
import co.ascendsoft.teclavya.project.entities.Projects;
import co.ascendsoft.teclavya.project.entities.RoadmapPhase;
import co.ascendsoft.teclavya.project.repo.ProjectPlanRepository;
import co.ascendsoft.teclavya.project.service.ProjectService;
import co.ascendsoft.teclavya.user.entities.User;
import co.ascendsoft.teclavya.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectPlanRepository projectPlanRepository;

    @Override
    public ProjectPlanResponse createProject(ProjectPreferenceRequest request) {

        User user = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        Projects project = Projects.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .title("AI-Powered To-Do App")
                .problemStatement("Users need a smart to-do list that categorizes and prioritizes tasks automatically.")
                .features(List.of(
                        "Task categorization",
                        "Priority prediction using ML",
                        "Voice input support",
                        "Reminders and notifications"
                ))
                .suggestedTechStack(List.of(
                        "Spring Boot",
                        "React",
                        "PostgreSQL",
                        "TensorFlow Lite"
                ))
                .dependencies(List.of(
                        "spring-boot-starter-data-jpa",
                        "spring-boot-starter-web",
                        "tensorflow-core-api"
                ))
                .tutorials(List.of(
                        "https://spring.io/guides/gs/rest-service/",
                        "https://reactjs.org/tutorial/tutorial.html"
                ))
                .prerequisites(List.of(
                        "Java 17",
                        "Basic ML knowledge",
                        "React fundamentals"
                ))
                .folderStructure("""
                        ├── backend
                        │   ├── src
                        │   └── pom.xml
                        ├── frontend
                        │   ├── src
                        │   └── package.json
                        └── README.md
                        """)
                .projectStarted(true)
                .projectEnded(false)
                .gitUrl("https://github.com/example/ai-todo-app")
                .hld("""
                        - Three-tier architecture
                        - RESTful APIs for communication
                        - Authentication and authorization using JWT
                        """)
                .lld("""
                        - Service layer handles business logic
                        - ML model inference integrated in task service
                        - React handles UI components for task management
                        """)
                .roadmap(List.of(
                        RoadmapPhase.builder()
                                .phase("Phase 1: Setup")
                                .description("Initialize project with backend and frontend skeleton.")
                                .estimatedTime("2 days")
                                .resources(List.of(
                                        "https://spring.io/guides",
                                        "https://create-react-app.dev/docs/getting-started/"
                                ))
                                .build(),
                        RoadmapPhase.builder()
                                .phase("Phase 2: Core Features")
                                .description("Develop task creation, editing, and smart sorting.")
                                .estimatedTime("1 week")
                                .resources(List.of(
                                        "https://reactjs.org/docs/hooks-intro.html",
                                        "https://tensorflow.org/js"
                                ))
                                .build()
                ))
                .build();

        projectPlanRepository.save(project);

        return toResponse(project);
    }

    private ProjectPlanResponse toResponse(Projects project) {
        return new ProjectPlanResponse(
                project.getTitle(),
                project.getProblemStatement(),
                project.getFeatures(),
                project.getSuggestedTechStack(),
                project.getFolderStructure(),
                project.getDependencies(),
                project.getTutorials(),
                project.getPrerequisites(),
                project.getRoadmap().stream()
                        .map(this::toRoadmapPhaseDto)
                        .toList()
        );
    }

    private co.ascendsoft.teclavya.project.dto.RoadmapPhase toRoadmapPhaseDto(RoadmapPhase entityPhase) {
        return new co.ascendsoft.teclavya.project.dto.RoadmapPhase(
                entityPhase.getPhase(),
                entityPhase.getDescription(),
                entityPhase.getEstimatedTime(),
                entityPhase.getResources()
        );
    }
}
