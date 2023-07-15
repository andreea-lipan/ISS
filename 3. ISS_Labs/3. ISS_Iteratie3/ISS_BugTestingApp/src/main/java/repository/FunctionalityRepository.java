package repository;

import model.Functionality;
import model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class FunctionalityRepository implements RepositoryInterface<Functionality> {

    private DatabaseUtils databaseUtils;

    public FunctionalityRepository(Properties properties) {
        this.databaseUtils = new DatabaseUtils(properties);
    }

    @Override
    public void add(Functionality functionality) {
        String insertStatement = "INSERT INTO app_functionalities(name, description, date_added, cod_project) VALUES (?, ?, ?, ?)";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, functionality.getName());
            preparedStatement.setString(2, functionality.getDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(functionality.getDateAdded()));
            preparedStatement.setInt(4, functionality.getProject().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding functionality to DB: " + ex);
        }
    }

    public Iterable<Functionality> getAllFunctionalities() {
        ArrayList<Functionality> functionalities = new ArrayList<>();
        Connection connection = databaseUtils.getConnection();
        String selectStatement = "SELECT * FROM app_functionalities";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while(resultSet.next()) {
                Integer id = resultSet.getInt("cod_functionality");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("date_added").toLocalDate();
                Integer idProject = resultSet.getInt("cod_project");

                Project project = new Project();
                project.setId(idProject);
                Functionality functionality = new Functionality(name, description, date, project);
                functionality.setId(id);
                functionalities.add(functionality);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting all functionalities from DB: " + ex);
        }
        return functionalities;
    }

    public void updateFunctionality(Functionality oldFunctionality, Functionality newFunctionality) {
        String updateStatement = "UPDATE app_functionalities SET name = ?, description = ?, date_added = ?, cod_project = ? WHERE cod_functionality = ?";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
            preparedStatement.setString(1, newFunctionality.getName());
            preparedStatement.setString(2, newFunctionality.getDescription());
            preparedStatement.setDate(3, java.sql.Date.valueOf(newFunctionality.getDateAdded()));
            preparedStatement.setInt(4, newFunctionality.getProject().getId());
            preparedStatement.setInt(5, oldFunctionality.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updating functionality in DB: " + ex);
        }
    }

    public void deleteFunctionality(Integer functionalityId) {
        String deleteStatement = "DELETE FROM app_functionalities WHERE cod_functionality = ?";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)) {
            preparedStatement.setInt(1, functionalityId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting functionality from DB: " + ex);
        }
    }
}
