package service;

import model.User;
import model.UserType;
import repository.RepositoryInterface;
import repository.UserHibernateRepo;
import repository.UserRepository;

public class UserService implements ServiceInterface {

    //private UserRepository userRepository;
    private UserHibernateRepo userRepository;

    public UserService(UserHibernateRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String username, String password) {
        User user = new User(username, password, null);
        return userRepository.findUser(user);
    }

}
