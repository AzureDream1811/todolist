package web.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private int priority;
    private LocalDate dueDate;
    private LocalDate completedAt;
    private Integer projectId; // nullable
    private int userId;
    private LocalDate createdAt;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isCompleted() {
        return this.completedAt != null;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getProjectId() {
        return projectId == null ? 0 : projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectIdObject() {
        return projectId;
    }

    public void setProjectIdObject(Integer projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", projectId='" + getProjectId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
    
}