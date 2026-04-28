package com.example.cateringapp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "project_collaborators")
public class ProjectCollaborator {

    @EmbeddedId
    private ProjectCollaboratorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_in_project", nullable = false, columnDefinition = "project_roles")
    private ProjectRole roleInProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;

    @Column(name = "added_at", nullable = false, insertable = false, updatable = false)
    private Instant addedAt;

    public ProjectCollaboratorId getId() {
        return id;
    }

    public void setId(ProjectCollaboratorId id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProjectRole getRoleInProject() {
        return roleInProject;
    }

    public void setRoleInProject(ProjectRole roleInProject) {
        this.roleInProject = roleInProject;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Instant getAddedAt() {
        return addedAt;
    }
}
