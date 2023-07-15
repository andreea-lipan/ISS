package repository;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class UserHibernateRepo implements RepositoryInterface<User> {
    static SessionFactory sessionFactory;
    public UserHibernateRepo() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);

        }
    }

    @Override
    public void add(User user) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public User findUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from User as u where u.username like :username and u.password like :password";
                User returnedUser =
                        session.createQuery(queryString, User.class)
                                .setParameter("username", user.getUsername())
                                .setParameter("password", user.getPassword())
                                .setMaxResults(1).uniqueResult();
                transaction.commit();
                return returnedUser;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }
}
