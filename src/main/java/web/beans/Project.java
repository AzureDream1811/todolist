package web.beans;

import java.time.LocalDate;

public class Project {
    private int id;
    private String name;
    private boolean favorite;
    private int userID;
    private LocalDate createdAt;

    public Project() {
    }

    public Project(int id, String name, boolean favorite, int userID, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.favorite = favorite;
        this.userID = userID;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", favorite=" + favorite +
                ", userID=" + userID +
                ", createdAt=" + createdAt +
                '}';
    }
}
