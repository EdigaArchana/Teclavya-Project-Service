package co.ascendsoft.teclavya.project.entities;

import co.ascendsoft.teclavya.user.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Projects {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String problemStatement;

    @ElementCollection
    private List<String> features;

    @ElementCollection
    private List<String> suggestedTechStack;

    @ElementCollection
    private List<String> dependencies;

    @ElementCollection
    private List<String> tutorials;

    @ElementCollection
    private List<String> prerequisites;

    @Column(length = 5000)
    private String folderStructure;

    private boolean projectStarted;
    private boolean projectEnded;

    private String gitUrl;

    @Column(length = 5000)
    private String hld;

    @Column(length = 5000)
    private String lld;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private List<RoadmapPhase> roadmap;
}
