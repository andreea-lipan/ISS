package service;

import model.Functionality;
import repository.FunctionalityRepository;
import repository.ProjectRepository;
import model.Project;

import java.time.LocalDate;

public class Project_FunctionalityService implements ServiceInterface {
    private ProjectRepository projectRepository;
    private FunctionalityRepository functionalityRepository;

    public Project_FunctionalityService(ProjectRepository projectRepository,
                                        FunctionalityRepository functionalityRepository) {
        this.projectRepository = projectRepository;
        this.functionalityRepository = functionalityRepository;
    }

    public Iterable<Project> getAllProjects() {
        return projectRepository.getAll();
    }

    public Iterable<Functionality> getAllFunctionalities() {
        Iterable<Functionality> functionalities = functionalityRepository.getAll();

        for (Functionality f : functionalities) {
            System.out.println(f);
            System.out.println(f.getProject().getId());
            f.setProject(projectRepository.find(f.getProject().getId()));
            System.out.println(f);
        }
        return functionalities;
    }

    public void addProject(String name) {
        Project project = new Project(name);
        projectRepository.add(project);
    }

    public void addFunctionality(String name, String description, LocalDate dateAdded, Project project) {
        Functionality functionality = new Functionality(name, description, dateAdded, project);
        functionalityRepository.add(functionality);
    }

    public void updateFunctionality(Functionality oldFunctionality, String newName,
                                    String newDescription, Project newProject) {
        // if params null keep initial data
        Functionality newFunctionality = new Functionality(newName, newDescription,
                oldFunctionality.getDateAdded(),newProject);
        if (newFunctionality.getName().isEmpty()) {
            newFunctionality.setName(oldFunctionality.getName());
        }
        if (newFunctionality.getDescription().isEmpty()) {
            newFunctionality.setDescription(oldFunctionality.getDescription());
        }
        if (newFunctionality.getProject() == null) {
            newFunctionality.setProject(oldFunctionality.getProject());
        }
        functionalityRepository.update(oldFunctionality, newFunctionality);
    }

    public void deleteFunctionality(Functionality functionality) {
        functionalityRepository.delete(functionality.getId());
    }


}
