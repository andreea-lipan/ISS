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
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;
import java.io.IOException;
import java.util.ArrayList;

public class UpdateFunctionalityController {
    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;
    private Functionality oldFunctionality;
    @FXML
    TextField nameTextField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    ComboBox<Project> projectComboBox;
    ObservableList<Project> projectsList = FXCollections.observableArrayList();

    void initialize(UserService userService, Project_FunctionalityService projectFunctionalityService,
                    BugService bugService, User loggedUser, Functionality oldFunctionality) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
        this.oldFunctionality = oldFunctionality;
        initializeVisuals();
    }

    void initializeVisuals() {
        nameTextField.setText(oldFunctionality.getName());
        descriptionTextArea.setText(oldFunctionality.getDescription());
        projectsList.setAll((ArrayList<Project>)projectFunctionalityService.getAllProjects());
        projectComboBox.setItems(projectsList);
        projectComboBox.setValue(oldFunctionality.getProject());
    }

    @FXML
    public void updateFunctionalityHandler() throws IOException {
        String newName = nameTextField.getText();
        String newDescription = descriptionTextArea.getText();
        Project newProject = projectComboBox.getSelectionModel().getSelectedItem();
        try {
            projectFunctionalityService.updateFunctionality(oldFunctionality, newName, newDescription, newProject);
        } catch (Exception ex) {
            System.out.println("Error updating funct in controller" + ex);
        }
        returnToAdminMainView("Update successful!");
    }
    @FXML
    public void cancelUpdateFunctionality(ActionEvent actionEvent) throws IOException {
        returnToAdminMainView("");
    }

    private void returnToAdminMainView(String message) throws IOException {
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
