package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.User;
import model.UserType;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;

public class LogInController {
    private UserService userService;
    private Project_FunctionalityService project_functionalityService;
    private BugService bugService;
    @FXML
    TextField usernameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Label errorMessageLabel;

    public void initialize(UserService userService, Project_FunctionalityService project_functionalityService, BugService bugService) {
        this.userService = userService;
        this.project_functionalityService = project_functionalityService;
        this.bugService = bugService;
        errorMessageLabel.setVisible(false);
    }

    public void logInHandler() throws IOException {
        errorMessageLabel.setVisible(false);
        String username = usernameTextField.getText();
        ;
        String password = passwordTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Invalid data!");
            errorMessageLabel.setVisible(true);
        } else {
            User loggedUser = userService.findUser(username, password);
            if (loggedUser == null) {
                errorMessageLabel.setText("Invalid data!");
                errorMessageLabel.setVisible(true);
            } else if (loggedUser.getType() == UserType.ADMIN) {
                /* changing to the main window according to user type */
                FXMLLoader fxmlLoader = new FXMLLoader();
                Scene scene = null;


                fxmlLoader.setLocation(LogInController.class.getResource("/AdminMainViewPretty.fxml"));
                scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
                AdminMainController adminMainController = fxmlLoader.getController();
                adminMainController.initializeAttributes(userService, project_functionalityService, bugService, loggedUser);
                adminMainController.initializeVisuals();
                stage.setScene(scene);

            } else if (loggedUser.getType() == UserType.TESTER) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                Scene scene = null;
                fxmlLoader.setLocation(LogInController.class.getResource("/TesterMainView.fxml"));
                scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
                TesterMainController testerMainController = fxmlLoader.getController();
                testerMainController.initializeAttributes(userService, project_functionalityService, bugService, loggedUser);
                testerMainController.initializeVisuals();
                stage.setScene(scene);

            } else if (loggedUser.getType() == UserType.PROGRAMMER) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                Scene scene = null;
                fxmlLoader.setLocation(LogInController.class.getResource("/ProgrammerMainView.fxml"));
                scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) errorMessageLabel.getScene().getWindow();
                ProgrammerMainController programmerMainController = fxmlLoader.getController();
                programmerMainController.initializeAttributes(userService, project_functionalityService, bugService, loggedUser);
                programmerMainController.initializeVisuals();
                stage.setScene(scene);

            }

        }

    }
}
