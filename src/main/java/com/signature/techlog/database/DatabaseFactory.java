package com.signature.techlog.database;

import com.signature.techlog.model.Archive;
import com.signature.techlog.model.Blog;
import com.signature.techlog.model.Comment;
import com.signature.techlog.model.OTP;
import com.signature.techlog.model.Reaction;
import com.signature.techlog.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DatabaseFactory {

    private static ServiceRegistry registry = null;
    private static SessionFactory factory = null;

    public static void openConnection() {
        Configuration configuration = new Configuration().configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Blog.class)
                .addAnnotatedClass(Reaction.class)
                .addAnnotatedClass(Comment.class)
                .addAnnotatedClass(OTP.class)
                .addAnnotatedClass(Archive.class);

        registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        factory = configuration.buildSessionFactory(registry);
    }

    public static SessionFactory getFactory() {
        return factory;
    }

    public static Session getSession() {
        return factory.openSession();
    }

    public static void closeConnection() {
        if (factory != null) {
            factory.close();
            if (registry != null) {
                registry.close();
            }
        }
    }
}