package co.ascendsoft.teclavya.project.repo;

import co.ascendsoft.teclavya.project.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPlanRepository extends JpaRepository<Projects, String> {
}
