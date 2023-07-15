package repository;

import model.User;
import model.UserType;

import java.lang.module.Configuration;
import java.sql.*;
import java.util.Properties;

public class UserRepository implements RepositoryInterface<User> {
    private DatabaseUtils databaseUtils;

    public UserRepository(Properties properties) {
        this.databaseUtils = new DatabaseUtils(properties);
    }

    @Override
    public void add(User user) {
        String insertStatement = "INSERT INTO app_users(username, password, type) VALUES (?, ?, ?)";
        Connection connection = databaseUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getType().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding User to DB: " + ex);
        }
    }

    public User findUser(User user) {
        Connection connection = databaseUtils.getConnection();
        String selectStatement = "SELECT * FROM app_users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            try (ResultSet result = preparedStatement.executeQuery()) {
                result.next();
                Integer id = result.getInt("cod_user");
                String username = result.getString("username");
                String password = result.getString("password");
                UserType userType = UserType.valueOf(result.getString("type"));

                User foundUser = new User(username, password, userType);
                foundUser.setId(id);

                return foundUser;
            }
        } catch (SQLException ex) {
            System.out.println("Error searching for User in DB: " + ex);
        }
        return null;
    }
}
