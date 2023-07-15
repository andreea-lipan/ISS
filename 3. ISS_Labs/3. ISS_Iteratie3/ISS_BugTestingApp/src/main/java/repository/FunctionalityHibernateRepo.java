package repository;

import model.Bug;
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


public class FunctionalityHibernateRepo implements RepositoryInterface<Functionality> {
    static SessionFactory sessionFactory;

    public FunctionalityHibernateRepo() {
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
    public void add(Functionality functionality) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(functionality);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public void deleteFunctionality(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //session.save(project);
                Functionality f = session.load(Functionality.class, id);
                session.delete(f);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public void updateFunctionality(Functionality oldFunc, Functionality newFunc) {
        newFunc.setId(oldFunc.getId());
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                //session.save(project);
                session.update(newFunc);
                transaction.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la update "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
    }

    public Iterable<Functionality> getAllFunctionalities() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                List<Functionality> borrowed =
                        session.createQuery("from Functionality as f LEFT JOIN FETCH f.project", Functionality.class)
                                .list();

                List<Functionality> finalFunc= new ArrayList<>();
                borrowed.forEach(x->{
                    Functionality b = session.load(Functionality.class,x.getId());
                    finalFunc.add(b);
                });
                transaction.commit();
                return finalFunc;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select "+ex);
                if (transaction != null)
                    transaction.rollback();
            }
        }
        return null;
    }
}

