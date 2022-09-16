package com.signature.techlog.database;

import com.signature.techlog.model.Archive;
import com.signature.techlog.model.Blog;
import com.signature.techlog.model.Comment;
import com.signature.techlog.model.Reaction;
import com.signature.techlog.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DatabaseFactory {

    private ServiceRegistry registry = null;
    private SessionFactory factory = null;

    private DatabaseFactory() { }

    public static DatabaseFactory getInstance() {
        return new DatabaseFactory();
    }

    public boolean openConnection() {
        try {
            Configuration configuration = new Configuration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Blog.class)
                    .addAnnotatedClass(Reaction.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(Archive.class);

            registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            factory = configuration.buildSessionFactory(registry);
            return Boolean.TRUE;
        } catch (NullPointerException | HibernateException ex) {
            return Boolean.FALSE;
        }
    }

    public SessionFactory getFactory() {
        return factory;
    }

    public Session getSession() {
        return factory.openSession();
    }

    public boolean closeConnection() {
        try {
            if (factory != null) {
                factory.close();
                if (registry != null) {
                    registry.close();
                }
            }
            return Boolean.TRUE;
        } catch (NullPointerException | IllegalStateException ex) {
            return Boolean.FALSE;
        }
    }
}