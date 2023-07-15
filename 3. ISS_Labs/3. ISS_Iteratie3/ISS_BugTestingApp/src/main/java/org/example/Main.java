package org.example;

import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.*;
import repository.*;
import service.BugService;
import service.Project_FunctionalityService;
import service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
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

        //UserRepository userRepository = new UserRepository(dbProperties);
        UserHibernateRepo userRepository = new UserHibernateRepo();
        //ProjectRepository projectRepository = new ProjectRepository(dbProperties);
        ProjectHibernateRepo projectRepository = new ProjectHibernateRepo();
        //FunctionalityRepository functionalityRepository = new FunctionalityRepository(dbProperties);
        FunctionalityHibernateRepo functionalityRepository = new FunctionalityHibernateRepo();
        //BugRepository bugRepository = new BugRepository(dbProperties);
        BugHibernateRepo bugRepository = new BugHibernateRepo();

        System.out.println("after repo");



//        Project p1 = new Project("Game");
//        Project p2 = new Project("Bank");
//        Project p3 = new Project("AudioBooks");
//
//        projectRepository.add(p1);
//        projectRepository.add(p2);
//        projectRepository.add(p3);
//
//        Functionality f1 = new Functionality("f1", "descr", LocalDate.now() , p1);
//        Functionality f2 = new Functionality("f2", "descr", LocalDate.now() , p1);
//        Functionality f3 = new Functionality("f3", "descr", LocalDate.now() , p2);
//        Functionality f4 = new Functionality("f4", "descr", LocalDate.now() , p2);
//        Functionality f5 = new Functionality("f5", "descr", LocalDate.now() , p3);
//        Functionality f6 = new Functionality("f6", "descr", LocalDate.now() , p3);
//
//        functionalityRepository.add(f1);
//        functionalityRepository.add(f2);
//        functionalityRepository.add(f3);
//        functionalityRepository.add(f4);
//        functionalityRepository.add(f5);
//        functionalityRepository.add(f6);
//
//        Bug b1 = new Bug("b1", "summary", f1, "desc", "1.play");
//        Bug b2 = new Bug("b2", "summary", f2, "desc", "1.play");
//        Bug b3 = new Bug("b3", "summary", f3, "desc", "1.play");
//        Bug b4 = new Bug("b4", "summary", f4, "desc", "1.play");
//        Bug b5 = new Bug("b5", "summary", f5, "desc", "1.play");
//        Bug b6 = new Bug("b6", "summary", f6, "desc", "1.play");
//
//        bugRepository.add(b1);
//        bugRepository.add(b2);
//        bugRepository.add(b3);
//        bugRepository.add(b4);
//        bugRepository.add(b5);
//        bugRepository.add(b6);
//


        UserService userService = new UserService (userRepository);
        Project_FunctionalityService project_functionalityService = new Project_FunctionalityService(projectRepository, functionalityRepository);
        BugService bugService = new BugService(bugRepository);


        System.out.println("after srv");

        /* --------------- Load Log In Window -------------- */
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/LogInView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogInController logInController = fxmlLoader.getController();
        logInController.initialize(userService, project_functionalityService, bugService);
        primaryStage.setScene(scene);
        primaryStage.show();



//        User newUser = new User("alex", "alex", UserType.ADMIN);
//        userRepository.add(newUser);
//        User newUser1 = new User("emma", "emma", UserType.PROGRAMMER);
//        userRepository.add(newUser1);
//        User newUser2 = new User("bob", "bob", UserType.TESTER);
//        userRepository.add(newUser2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeView(primaryStage);
    }
}