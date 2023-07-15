package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
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
    @FXML
    ListView functionalitiesListView;
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
}
