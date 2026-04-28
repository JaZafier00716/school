package com.example.cateringapp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "project_roles")
    private ProjectRole name;

    @Column(name = "can_manage_users", nullable = false)
    private boolean canManageUsers;

    @Column(name = "can_manage_projects", nullable = false)
    private boolean canManageProjects;

    @Column(name = "can_publish", nullable = false)
    private boolean canPublish;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    public Long getRoleId() {
        return roleId;
    }

    public ProjectRole getName() {
        return name;
    }

    public void setName(ProjectRole name) {
        this.name = name;
    }

    public boolean isCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }

    public boolean isCanManageProjects() {
        return canManageProjects;
    }

    public void setCanManageProjects(boolean canManageProjects) {
        this.canManageProjects = canManageProjects;
    }

    public boolean isCanPublish() {
        return canPublish;
    }

    public void setCanPublish(boolean canPublish) {
        this.canPublish = canPublish;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
