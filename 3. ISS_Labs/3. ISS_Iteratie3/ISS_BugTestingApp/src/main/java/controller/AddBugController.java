package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Functionality;
import model.Project;
import model.User;
import model.UserType;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class AddBugController {
    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;

    @FXML
    TextField nameTextField;
    @FXML
    TextField summaryTextField;
    @FXML
    ComboBox<Functionality> functionalityComboBox;
    ObservableList<Functionality> functionalitiesList = FXCollections.observableArrayList();
    @FXML
    TextArea descriptionTextArea;
    @FXML
    TextArea stepsToReproduceTextArea;


    public void initialize(UserService userService, Project_FunctionalityService projectFunctionalityService, BugService bugService, User loggedUser) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
        initVisuals();
    }

    public void initVisuals() {
        functionalitiesList.setAll((ArrayList<Functionality>) projectFunctionalityService.getAllFunctionalities());
        functionalityComboBox.setItems(functionalitiesList);
    }


    public void addBugHandler(ActionEvent actionEvent) throws IOException {
        String name = nameTextField.getText();
        String summary = summaryTextField.getText();
        Functionality functionality = functionalityComboBox.getSelectionModel().getSelectedItem();
        String description = descriptionTextArea.getText();
        String stepsToReproduce = stepsToReproduceTextArea.getText();

        try {
            bugService.addBug(name, summary, functionality, description, stepsToReproduce);
        } catch (Exception ex) {
            returnToMainAdminView("Error adding the bug!");
            System.out.println("Error adding bug in controller");
            return;
        }


        if (loggedUser.getType() == UserType.ADMIN) {
            returnToMainAdminView("Bug added!");
        } else if (loggedUser.getType() == UserType.TESTER) {
            returnToMainTesterView("Bug added!");
        }
    }

    public void cancelAddingBugHandler(ActionEvent actionEvent) throws IOException {
        if (loggedUser.getType() == UserType.ADMIN) {
            returnToMainAdminView("");
        } else if (loggedUser.getType() == UserType.TESTER) {
            returnToMainTesterView("");
        }
    }

    private void returnToMainAdminView(String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(LogInController.class.getResource("/AdminMainViewPretty.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        AdminMainController adminMainController = fxmlLoader.getController();
        adminMainController.initializeAttributes(userService, projectFunctionalityService, bugService, loggedUser);
        adminMainController.initializeVisuals();
        if (!message.isEmpty()) {
            adminMainController.showStatus(message);
        }
        stage.setScene(scene);
    }

    private void returnToMainTesterView(String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(LogInController.class.getResource("/TesterMainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        TesterMainController testerMainController = fxmlLoader.getController();
        testerMainController.initializeAttributes(userService, projectFunctionalityService, bugService, loggedUser);
        testerMainController.initializeVisuals();
        if (!message.isEmpty()) {
            testerMainController.showStatus(message);
        }
        stage.setScene(scene);
    }
}
