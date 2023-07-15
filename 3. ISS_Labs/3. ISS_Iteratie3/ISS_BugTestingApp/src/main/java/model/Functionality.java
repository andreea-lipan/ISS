package model;

import java.time.LocalDate;

public class Functionality implements Entity<Integer>{
    private Integer id;
    private String name;
    private String description;
    private LocalDate dateAdded;
    private Project project;

    public Functionality(String name, String description, LocalDate dateAdded, Project project) {
        this.name = name;
        this.description = description;
        this.dateAdded = dateAdded;
        this.project = project;
    }

    public Functionality() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "F{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateAdded=" + dateAdded +
                ", project=" + project +
                '}';
    }
}
