package repository;

import model.Bug;
import model.Functionality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class BugRepository implements RepositoryInterface<Bug> {

    private DatabaseUtils databaseUtils;

    public BugRepository(Properties properties) {
        this.databaseUtils = new DatabaseUtils(properties);
    }

    @Override
    public void add(Bug bug) {
        String insertStatement = "INSERT INTO app_bugs(name, summary, cod_functionality, description, stepsToReproduce) VALUES (?, ?, ?, ?, ?)";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, bug.getName());
            preparedStatement.setString(2, bug.getSummary());
            preparedStatement.setInt(3, bug.getFunctionality().getId());
            preparedStatement.setString(4, bug.getDescription());
            preparedStatement.setString(5, bug.getStepsToReproduce());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding bug to DB: " + ex);
        }
    }

    public Iterable<Bug> getAllBugs() {
        ArrayList<Bug> bugs = new ArrayList<>();
        Connection connection = databaseUtils.getConnection();
        String selectStatement = "SELECT * FROM app_bugs";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while(resultSet.next()) {
                Integer id = resultSet.getInt("cod_bug");
                String name = resultSet.getString("name");
                String summary = resultSet.getString("summary");
                Integer idFunctionality = resultSet.getInt("cod_functionality");
                String description = resultSet.getString("description");
                String stepsToReproduce = resultSet.getString("stepsToReproduce");

                Functionality functionality = new Functionality();
                functionality.setId(idFunctionality);
                Bug bug = new Bug(name, summary, functionality, description, stepsToReproduce);
                bug.setId(id);
                bugs.add(bug);
            }
        } catch (SQLException ex) {
            System.out.println("Error getting all bugs from DB: " + ex);
        }
        return bugs;
    }

    public void updateBug(Bug oldBug, Bug newBug) {
        String updateStatement = "UPDATE app_bugs SET name = ?, summary = ?, cod_functionality = ?, description = ?, stepsToReproduce = ? WHERE cod_bug = ?";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {

            preparedStatement.setString(1, newBug.getName());
            preparedStatement.setString(2, newBug.getSummary());
            preparedStatement.setInt(3, newBug.getFunctionality().getId());
            preparedStatement.setString(4, newBug.getDescription());
            preparedStatement.setString(5, newBug.getStepsToReproduce());
            preparedStatement.setInt(6, oldBug.getId());


            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updating bug in DB: " + ex);
        }
    }

    public void deleteBug(Integer bugId) {
        String deleteStatement = "DELETE FROM app_bugs WHERE cod_bug = ?";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)) {
            preparedStatement.setInt(1, bugId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting bug from DB: " + ex);
        }
    }

}
