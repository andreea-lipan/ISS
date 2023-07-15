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
import model.Bug;
import model.Functionality;
import model.User;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateBugController {

    private UserService userService;
    private Project_FunctionalityService projectFunctionalityService;
    private BugService bugService;
    private User loggedUser;
    private Bug oldBug;
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
    public void initialize(UserService userService, Project_FunctionalityService projectFunctionalityService, BugService bugService, User loggedUser, Bug oldBug) {
        this.userService = userService;
        this.projectFunctionalityService = projectFunctionalityService;
        this.bugService = bugService;
        this.loggedUser = loggedUser;
        this.oldBug = oldBug;
        initVisuals();
    }

    public void initVisuals() {
        functionalitiesList.setAll((ArrayList<Functionality>) projectFunctionalityService.getAllFunctionalities());
        functionalityComboBox.setItems(functionalitiesList);

        nameTextField.setText(oldBug.getName());
        descriptionTextArea.setText(oldBug.getDescription());
        summaryTextField.setText(oldBug.getSummary());
        stepsToReproduceTextArea.setText(oldBug.getStepsToReproduce());
        functionalityComboBox.setValue(oldBug.getFunctionality());
    }

    public void updateBugHandler(ActionEvent actionEvent) throws IOException {
        String newName = nameTextField.getText();
        String newDescription = descriptionTextArea.getText();
        String newSummary = summaryTextField.getText();
        String newStepsToReproduce = stepsToReproduceTextArea.getText();
        Functionality newFunctionality = functionalityComboBox.getSelectionModel().getSelectedItem();
        try {
            bugService.updateBug(oldBug, newName, newSummary, newFunctionality, newDescription, newStepsToReproduce);
        } catch (Exception ex) {
            System.out.println("Error updating bug in controller" + ex);
        }
        returnToMainAdminView("Update successful!");
    }

    public void cancelUpdatingBugHandler(ActionEvent actionEvent) throws IOException {
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
