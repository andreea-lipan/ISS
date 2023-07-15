package org.example;

import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import repository.BugRepository;
import repository.FunctionalityRepository;
import repository.ProjectRepository;
import repository.UserRepository;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(args);
    }

    private static void initializeView(Stage primaryStage) throws IOException {

       // primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon_app_icons.png")));

        Properties dbProperties = new Properties();
        try {
            dbProperties.load(Main.class.getResourceAsStream("/db.properties"));
            dbProperties.list(System.out);
        } catch (IOException ex) {
            System.out.println("Error retriving db connection info: " + ex);
        }

        UserRepository userRepository = new UserRepository(dbProperties);
        ProjectRepository projectRepository = new ProjectRepository(dbProperties);
        FunctionalityRepository functionalityRepository = new FunctionalityRepository(dbProperties);
        BugRepository bugRepository = null;

        System.out.println("after repo");

        UserService userService = new UserService (userRepository);
        Project_FunctionalityService project_functionalityService = new Project_FunctionalityService(projectRepository, functionalityRepository);
        BugService bugService = null;


        System.out.println("after srv");

        /* --------------- Load Log In Window -------------- */
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/LogInView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogInController logInController = fxmlLoader.getController();
        logInController.initialize(userService, project_functionalityService, bugService);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeView(primaryStage);
    }
}