package repository;

import model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ProjectRepository implements RepositoryInterface<Project>{
    private DatabaseUtils databaseUtils;

    public ProjectRepository(Properties properties) {
        this.databaseUtils = new DatabaseUtils(properties);
    }

    @Override
    public void add(Project project) {
        String insertStatement = "INSERT INTO app_projects (name) VALUES (?)";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, project.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting project into DB: " + ex);
        }
    }

    public Iterable<Project> getAll() {
        ArrayList<Project> projects = new ArrayList<>();
        String selectStatement = "SELECT * FROM app_projects";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
             ResultSet resultSet = preparedStatement.executeQuery())   {
            while (resultSet.next()) {
                Integer projectId = resultSet.getInt("cod_project");
                String name = resultSet.getString("name");

                Project project = new Project(name);
                project.setId(projectId);

                projects.add(project);
            }
        } catch (SQLException ex) {
            System.out.println("Error get all projects from DB: " + ex);
        }
        return projects;
    }

    public Project find(Integer id) {
        String selectStatement = "SELECT * FROM app_projects WHERE cod_project = ?";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String name = resultSet.getString("name");

            Project project = new Project(name);
            project.setId(id);

            return project;

        }  catch (SQLException ex) {
            System.out.println("Error get project by id from DB: " + ex);
        }
        return null;
    }
}
