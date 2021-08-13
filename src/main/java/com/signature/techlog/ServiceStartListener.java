package com.signature.techlog;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Blog;
import com.signature.techlog.model.Comment;
import com.signature.techlog.model.Reaction;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServiceStartListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(ServiceStartListener.class);

    private static ScheduledFuture<?> scheduledOtpCleaner;

    public ServiceStartListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Started Initializing Context...");

        ServletContextListener.super.contextInitialized(sce);
        DatabaseFactory.openConnection();

        try (Session session = DatabaseFactory.getSession()) {
            Object result = session.createSQLQuery("SELECT MAX(BLOG_ID) FROM Blog").getSingleResult();
            if (result != null) {
                Blog.setBlogCount(((Number) result).intValue() + 1);
            } else {
                Blog.setBlogCount(1);
            }

            result = session.createSQLQuery("SELECT MAX(REACTION_ID) FROM Reaction").getSingleResult();
            if (result != null) {
                Reaction.setReactionCount(((Number) result).intValue() + 1);
            } else {
                Reaction.setReactionCount(1);
            }

            result = session.createSQLQuery("SELECT MAX(COMMENT_ID) FROM Comment").getSingleResult();
            if (result != null) {
                Comment.setCommentCount(((Number) result).intValue() + 1);
            } else {
                Comment.setCommentCount(1);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        LOGGER.info("Registering Scheduled OTP Dump Cleanup Thread");
        scheduledOtpCleaner = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Scheduled OTP Dump Cleanup Thread");
                thread.setDaemon(Boolean.TRUE);
                return thread;
            }

        }).schedule(new Runnable() {

            @Override
            public void run() {
                Transaction tx = null;
                try (Session session = DatabaseFactory.getSession()) {
                    tx = session.beginTransaction();

                    Timestamp timestamp = Timestamp.from(Instant.now());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(timestamp.getTime());
                    calendar.add(Calendar.MINUTE, -16);

                    timestamp = new Timestamp(calendar.getTime().getTime());

                    session.createQuery("DELETE FROM OTP WHERE TIMESTAMP <= ?1").setParameter(1, timestamp)
                            .executeUpdate();

                    tx.commit();
                } catch (Exception ex) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                }
            }
        }, 1, TimeUnit.DAYS);

        LOGGER.info("Finished Initializing Context...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Started Destroying Context...");
        ServletContextListener.super.contextDestroyed(sce);

        DatabaseFactory.closeConnection();

        LOGGER.info("Calling MySQL AbandonedConnectionCleanupThread shutdown");
        AbandonedConnectionCleanupThread.checkedShutdown();
        LOGGER.info("MySQL AbandonedConnectionCleanupThread shutdown completed");

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            if (driver.getClass().getClassLoader() == cl) {

                try {
                    LOGGER.info("Deregistering JDBC driver {}", driver.toString());
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    LOGGER.error("Error deregistering JDBC driver {}", driver, ex);
                }

            } else {
                LOGGER.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader",
                        driver);
            }
        }

        LOGGER.info("Calling Scheduled OTP Dump Cleanup Thread shutdonw");
        if (!scheduledOtpCleaner.isDone()) {

            while (!scheduledOtpCleaner.isCancelled()) {
                scheduledOtpCleaner.cancel(true);
            }
            LOGGER.info("Scheduled OTP Dump Cleanup Thread shutdown completed");

        } else {
            LOGGER.info("Scheduled OTP Dump Cleanup Thread shutdown completed");
        }
        
        LOGGER.info("Finished Destroying Context...");
    }

}