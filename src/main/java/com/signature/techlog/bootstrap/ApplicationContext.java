package com.signature.techlog.bootstrap;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.repository.ArchiveRepository;
import com.signature.techlog.repository.BlogRepository;
import com.signature.techlog.repository.CommentRepository;
import com.signature.techlog.repository.ReactionRepository;
import com.signature.techlog.repository.UserRepository;
import com.signature.techlog.repository.impl.ArchiveRepositoryImpl;
import com.signature.techlog.repository.impl.BlogRepositoryImpl;
import com.signature.techlog.repository.impl.ReactionRepositoryImpl;
import com.signature.techlog.repository.impl.UserRepositoryImpl;
import com.signature.techlog.service.impl.GoogleOAuthService;
import com.signature.techlog.service.impl.StandardAuthService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

@WebListener
public class ApplicationContext implements ServletContextListener {

    private final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);

    private static final Properties properties = new Properties();
    private static final Properties messages = new Properties();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Started Initializing Application Context...");
        ServletContextListener.super.contextInitialized(sce);
        if (loadApplicationProperties()) {
            if (loadApplicationBeans(sce.getServletContext())) {
                if (loadApplicationDataSource(sce.getServletContext())) {
                    LOGGER.info("Finished Initializing Application Context...");
                } else {
                    LOGGER.error("Failed Initializing Application Context...");
                    throw new RuntimeException("Failed Initializing Application Context...");
                }
            } else {
                LOGGER.error("Failed Initializing Application Context...");
                throw new RuntimeException("Failed Initializing Application Context...");
            }
        } else {
            LOGGER.error("Failed Initializing Application Context...");
            throw new RuntimeException("Failed Initializing Application Context...");
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static Properties getMessages() {
        return messages;
    }

    private boolean loadApplicationProperties() {
        try {
            LOGGER.info("Loading Application Properties...");
            properties.load(ApplicationContext.class.getClassLoader().getResourceAsStream("application.properties"));
            messages.load(ApplicationContext.class.getClassLoader().getResourceAsStream("messages.properties"));
            return Boolean.TRUE;
        } catch (NullPointerException | IOException ex) {
            LOGGER.error("Failed to load application properties...", ex);
            return Boolean.FALSE;
        }
    }

    private boolean loadApplicationBeans(ServletContext context) {
        try {
            LOGGER.info("Initializing Application Beans...");

            LOGGER.info("Initializing Database Factory Bean...");
            context.setAttribute(DatabaseFactory.class.getSimpleName(), DatabaseFactory.getInstance());

            LOGGER.info("Initializing Repository Beans...");
            context.setAttribute(UserRepository.class.getSimpleName(), UserRepositoryImpl.getInstance((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())));
            context.setAttribute(BlogRepository.class.getSimpleName(), BlogRepositoryImpl.getInstance((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())));
            context.setAttribute(ReactionRepository.class.getSimpleName(), ReactionRepositoryImpl.getInstance((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())));
            context.setAttribute(CommentRepository.class.getSimpleName(), ReactionRepositoryImpl.getInstance((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())));
            context.setAttribute(ArchiveRepository.class.getSimpleName(), ArchiveRepositoryImpl.getInstance((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())));

            LOGGER.info("Initializing Service Beans...");
            context.setAttribute(StandardAuthService.class.getSimpleName(), StandardAuthService.getInstance((UserRepository) context.getAttribute(UserRepository.class.getSimpleName())));
            context.setAttribute(GoogleOAuthService.class.getSimpleName(), GoogleOAuthService.getInstance((UserRepository) context.getAttribute(UserRepository.class.getSimpleName())));

            return Boolean.TRUE;
        } catch (NullPointerException ex) {
            LOGGER.error("Failed to initialize application beans...", ex);
            return Boolean.FALSE;
        }
    }

    private boolean loadApplicationDataSource(ServletContext context) {
        LOGGER.info("Initializing Application Data Source...");
        return ((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())).openConnection();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Started Destroying Application Context...");
        ServletContextListener.super.contextDestroyed(sce);
        if (destroyApplicationDataSource(sce.getServletContext())) {
            if (destroyApplicationBeans(sce.getServletContext())) {
                LOGGER.info("Finished Destroying Application Context...");
            } else {
                LOGGER.error("Failed to destroy application context!");
            }
        } else {
            LOGGER.error("Failed to destroy application context!");
        }
    }

    private boolean destroyApplicationDataSource(ServletContext context) {
        LOGGER.info("Destroying Application Data Source...");
        if (((DatabaseFactory) context.getAttribute(DatabaseFactory.class.getSimpleName())).closeConnection()) {
            LOGGER.info("Calling MySQL AbandonedConnectionCleanupThread shutdown");
            AbandonedConnectionCleanupThread.checkedShutdown();
            LOGGER.info("MySQL AbandonedConnectionCleanupThread shutdown completed");

            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();

                if (driver.getClass().getClassLoader() == cl) {
                    try {
                        LOGGER.info("De-registering JDBC driver {}", driver.toString());
                        DriverManager.deregisterDriver(driver);
                    } catch (SQLException ex) {
                        LOGGER.error("Error de-registering JDBC driver {}", driver, ex);
                    }
                } else {
                    LOGGER.trace("Not de-registering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
                }
            }
            return Boolean.TRUE;
        } else {
            LOGGER.error("Failed to destroy application data source...");
            return Boolean.FALSE;
        }
    }

    private boolean destroyApplicationBeans(ServletContext context) {
        try {
            LOGGER.info("Destroying Application Beans...");
            context.getAttributeNames().asIterator().forEachRemaining(context::removeAttribute);
            return Boolean.TRUE;
        } catch (NullPointerException ex) {
            LOGGER.error("Failed to destroy application beans...", ex);
            return Boolean.FALSE;
        }
    }
}