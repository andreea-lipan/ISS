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
import model.User;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class TesterMainController {

    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;
    ObservableList<Bug> allBugsList = FXCollections.observableArrayList();
    @FXML
    ListView<Bug> bugsListView;
    @FXML
    Label statusLabel;
    @FXML
    Label usernameLabel;
    public void initializeAttributes(UserService userService, Project_FunctionalityService projectFunctionalityService, BugService bugService, User loggedUser) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
    }

    public void initializeVisuals () {
        usernameLabel.setText("Welcome: " + loggedUser.getUsername());
        statusLabel.setVisible(false);
        populateBugsTable();
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

    public void addBugHandler(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(AdminMainController.class.getResource("/AddBugView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) bugsListView.getScene().getWindow();
        AddBugController addBugController = fxmlLoader.getController();
        addBugController.initialize(userService, projectFunctionalityService, bugService, loggedUser);
        stage.setScene(scene);
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
