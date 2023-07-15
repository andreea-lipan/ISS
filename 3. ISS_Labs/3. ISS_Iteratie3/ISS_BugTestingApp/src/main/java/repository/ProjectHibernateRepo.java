package repository;

import model.Functionality;
import model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProjectHibernateRepo implements RepositoryInterface<Project>{
    static SessionFactory sessionFactory;

    public ProjectHibernateRepo() {
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
    public void add(Project project) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(project);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public Project find(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //session.save(project);
                Project p = session.load(Project.class, id);
                transaction.commit();
                return p;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }

    public Iterable<Project> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Project> projects =
                        session.createQuery("from Project as p", Project.class)
                                .list();

                transaction.commit();
                return projects;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }
}
