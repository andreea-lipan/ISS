package service;

import model.Bug;
import model.Functionality;
import repository.BugHibernateRepo;
import repository.BugRepository;

import java.util.ArrayList;
import java.util.List;

public class BugService implements ServiceInterface{

    //private BugRepository bugRepository;
    private BugHibernateRepo bugRepository;


    public BugService(BugHibernateRepo bugRepository) {
        this.bugRepository = bugRepository;
    }

    public Iterable<Bug> getAllBugs() {
        return bugRepository.getAllBugs();
    }

    public void addBug(String name, String summary, Functionality functionality, String description, String stepsToReproduce) {

        Bug bug = new Bug(name, summary, functionality, description, stepsToReproduce);
        bugRepository.add(bug);
    }

    public void updateBug(Bug oldBug, String newName, String newSummary, Functionality newFunctionality , String newDescription, String newStepsToReproduce) {
        Bug newBug = new Bug(newName, newSummary, newFunctionality, newDescription, newStepsToReproduce);
        newBug.setId(oldBug.getId());
        System.out.println("new bug in update srv: " + newBug);
        if (newName.isEmpty()) {
            newBug.setName(oldBug.getName());
        }
        if (newSummary.isEmpty()) {
            newBug.setSummary(oldBug.getSummary());
        }
        if (newFunctionality == null) {
            newBug.setFunctionality(oldBug.getFunctionality());
        }
        if (newDescription.isEmpty()) {
            newBug.setDescription(oldBug.getDescription());
        }
        if (newStepsToReproduce.isEmpty()) {
            newBug.setStepsToReproduce(oldBug.getStepsToReproduce());
        }

        bugRepository.updateBug(oldBug, newBug);
    }

    public void deleteBug(Bug bug) {
        bugRepository.deleteBug(bug.getId());
    }

    public Iterable<Bug> sortBugs(ArrayList<Bug> bugs) {
        List<Bug> sortedBugs =  bugs.stream().sorted((a, b) -> {return a.getName().compareTo(b.getName());}).toList();
        return sortedBugs;
    }
}
