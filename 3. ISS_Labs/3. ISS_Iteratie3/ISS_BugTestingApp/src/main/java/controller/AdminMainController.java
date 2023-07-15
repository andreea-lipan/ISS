package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Bug;
import model.Functionality;
import model.User;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class AdminMainController {

    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;
    ObservableList<Functionality> allFunctionalitiesList = FXCollections.observableArrayList();
    ObservableList<Bug> allBugsList = FXCollections.observableArrayList();
    @FXML
    ListView functionalitiesListView;
    @FXML
    ListView bugsListView;
    @FXML
    Label usernameLabel;
    @FXML
    Label statusLabel;
    public void initializeAttributes(UserService userService, Project_FunctionalityService projectFunctionalityService, BugService bugService, User loggedUser) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
    }
    public  void initializeVisuals() {
        System.out.println("init v");
        populateFunctionalityTable();
        populateBugsTable();
        usernameLabel.setText("Welcome: " + loggedUser.getUsername());
        statusLabel.setVisible(false);
    }

    public void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                statusLabel.setVisible(false);
            }
        }, 2000);
    }

    private void populateFunctionalityTable() {
        functionalitiesListView.setItems(null);
        ArrayList<Functionality> functionalities = (ArrayList<Functionality> )projectFunctionalityService.getAllFunctionalities();
        allFunctionalitiesList.setAll(functionalities);
        System.out.println("functionalities");
        functionalitiesListView.setItems(allFunctionalitiesList);
        functionalities.forEach(System.out::println);
        System.out.println("functionalities LIST VIEW");
        functionalitiesListView.getItems().forEach(System.out::println);
    }

    private void populateBugsTable() {

        bugsListView.setItems(null);
        ArrayList<Bug> bugs = (ArrayList<Bug>) bugService.getAllBugs();
        allBugsList.setAll(bugs);

        System.out.println("bugs");
        bugsListView.setItems(allBugsList);
        bugs.forEach(System.out::println);
        System.out.println("bugs LIST VIEW");
        bugsListView.getItems().forEach(System.out::println);
    }

    public void addFunctionality(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AdminMainController.class.getResource("/AddFunctionalityView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) functionalitiesListView.getScene().getWindow();
        AddFunctionalityController addFunctionalityController = fxmlLoader.getController();
        addFunctionalityController.initialize(userService, projectFunctionalityService, bugService, loggedUser);
        stage.setScene(scene);
    }


    public void updateFunctionality(ActionEvent actionEvent) throws IOException {
        //get selected funct
        Functionality selectedFunctionality = (Functionality) functionalitiesListView.getSelectionModel().getSelectedItem();
        if (selectedFunctionality == null) {
            showStatus("Error getting the selected item!");
            return;
        }
        //change view
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AdminMainController.class.getResource("/UpdateFunctionalityView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) functionalitiesListView.getScene().getWindow();
        UpdateFunctionalityController updateFunctionalityController = fxmlLoader.getController();
        updateFunctionalityController.initialize(userService, projectFunctionalityService, bugService, loggedUser, selectedFunctionality);
        stage.setScene(scene);
    }

    public void removeFunctionality(ActionEvent actionEvent) {
        Functionality functionality = (Functionality) functionalitiesListView.getSelectionModel().getSelectedItem();
        if (functionality == null) {
            showStatus("Error getting the selected item!");
            return;
        }
        System.out.println("Deleting Functionality: " + functionality);
        allFunctionalitiesList.remove(functionality);
        try {
            projectFunctionalityService.deleteFunctionality(functionality);
            showStatus("Functionality removed!");
        } catch (Exception e) {
            System.out.println("Error when trying to delete functionality in controller");
        }

    }

    public void sortBugs(ActionEvent actionEvent) {
        ArrayList<Bug> bugs = (ArrayList<Bug>) bugService.getAllBugs();
        ArrayList<Bug> sortedBugs = new ArrayList<>();

        for (var el : bugService.sortBugs(bugs)) {
            sortedBugs.add(el);
        }
        //bugService.sortBugs(bugs);

        allBugsList.setAll(sortedBugs);
    }

    public void sortFuncionalities(ActionEvent actionEvent) {
        ArrayList<Functionality> funcs = (ArrayList<Functionality>) projectFunctionalityService.getAllFunctionalities();
        ArrayList<Functionality> sortedFuncs = new ArrayList<>();

        for (var el : projectFunctionalityService.sortFunctionalities(funcs)) {
            sortedFuncs.add(el);
        }
        //bugService.sortBugs(bugs);

        allFunctionalitiesList.setAll(sortedFuncs);
    }

    public void addBugHandler(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AdminMainController.class.getResource("/AddBugView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) bugsListView.getScene().getWindow();
        AddBugController addBugController = fxmlLoader.getController();
        addBugController.initialize(userService, projectFunctionalityService, bugService, loggedUser);
        stage.setScene(scene);
    }

    public void updateBugHandler(ActionEvent actionEvent) throws IOException {
        //get selected funct
        Bug selectedBug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (selectedBug == null) {
            showStatus("Error getting the selected item!");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AdminMainController.class.getResource("/UpdateBugView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) bugsListView.getScene().getWindow();
        UpdateBugController updateBugController = fxmlLoader.getController();
        updateBugController.initialize(userService, projectFunctionalityService, bugService, loggedUser, selectedBug);
        stage.setScene(scene);
    }

    public void removeBugHandler(ActionEvent actionEvent) {
        Bug bug = (Bug) bugsListView.getSelectionModel().getSelectedItem();
        if (bug == null) {
            showStatus("Error getting the selected item!");
            return;
        }
        System.out.println("Deleting Bug: " + bug);
        allBugsList.remove(bug);
        try {
            bugService.deleteBug(bug);
            showStatus("Bug removed!");
        } catch (Exception e) {
            System.out.println("Error when trying to delete bug in controller");
        }

    }

    public void logOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Scene scene = null;
        fxmlLoader.setLocation(LogInController.class.getResource("/LogInView.fxml"));
        scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        LogInController logInController = fxmlLoader.getController();
        logInController.initialize(userService, projectFunctionalityService, bugService);
        stage.setScene(scene);
    }


}
