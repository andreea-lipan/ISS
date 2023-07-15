package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Project;
import model.User;
import model.UserType;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddFunctionalityController {
    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;
    @FXML
    TextField nameTextField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    DatePicker dateDatePicker;
    @FXML
    ComboBox<Project> projectComboBox;

    ObservableList<Project> projectsList = FXCollections.observableArrayList();

    void initialize(UserService userService, Project_FunctionalityService projectFunctionalityService,
                    BugService bugService, User loggedUser) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
        initializeVisuals();
    }

    void initializeVisuals() {
        projectsList.setAll((ArrayList<Project>)projectFunctionalityService.getAllProjects());
        projectComboBox.setItems(projectsList);
    }

    @FXML
    void addFunctionalityHandler() throws IOException {
        String name = nameTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate addedDate = dateDatePicker.getValue();
        Project project = projectComboBox.getSelectionModel().getSelectedItem();
        try {
            projectFunctionalityService.addFunctionality(name, description, addedDate, project);
        } catch (Exception ex) {
            returnToMainAdminView("Error adding the functionality!");
            System.out.println("Error adding func in controller");
            return;
        }
        returnToMainAdminView("Functionality added!");
    }
    @FXML
    public void cancelAddingFunctionalityHandler() throws IOException {
        returnToMainAdminView("");
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
}
