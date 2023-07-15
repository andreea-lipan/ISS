package repository;

import model.Bug;
import model.Functionality;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class BugHibernateRepo implements RepositoryInterface<Bug>{
    static SessionFactory sessionFactory;

    public BugHibernateRepo() {
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
    public void add(Bug bug) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(bug);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public void deleteBug(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //session.save(project);
                Bug b = session.load(Bug.class, id);
                session.delete(b);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la delete "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public void updateBug(Bug oldBug, Bug newBug) {
        newBug.setId(oldBug.getId());
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //session.save(project);
                session.update(newBug);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la update "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public Iterable<Bug> getAllBugs() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Bug> bugs =
                        session.createQuery("from Bug as b LEFT JOIN FETCH b.functionality LEFT JOIN FETCH b.functionality.project ", Bug.class)
                                .list();

                List<Bug> finalBug= new ArrayList<>();
                bugs.forEach(x->{
                    Bug b = session.load(Bug.class,x.getId());
                    finalBug.add(b);
                });
                transaction.commit();
                return finalBug;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }
}
