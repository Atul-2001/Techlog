package com.signature.techlog.data;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.User;

import jakarta.persistence.PersistenceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserHandler {

    private final Logger LOGGER = LogManager.getLogger(UserHandler.class);

    private UserHandler() {
    }

    public static UserHandler getInstance() {
        return new UserHandler();
    }

    public boolean saveUser(User user) {
        if (user != null) {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.save(user);
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR,
                        "Process : Persist User in Database | Cause : Exception occured while handling transaction failure. | Loss : Failed to rollback Transaction",
                        ex);
                response = Boolean.FALSE;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "User is not available (null)");
            return Boolean.FALSE;
        }
    }

    public boolean updateUser(User user) {
        if (user != null) {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.update(user);
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR,
                        "Process : Update User in Database | Cause : Exception occured while handling transaction failure. | Loss : Failed to rollback Transaction",
                        ex);
                response = Boolean.FALSE;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "User is not available (null)");
            return Boolean.FALSE;
        }
    }

    public boolean saveOrUpdateUser(User user) {
        if (user != null) {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.saveOrUpdate(user);
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR,
                        "Process : Save or Update User in Database | Cause : Exception occured while handling transaction failure. | Loss : Failed to rollback Transaction",
                        ex);
                response = Boolean.FALSE;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "User is not available (null)");
            return Boolean.FALSE;
        }
    }

    public User getUserByID(String id) {
        User response;
        try (Session session = DatabaseFactory.getSession()) {
            response = session.get(User.class, id);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, ex.getMessage(), ex);
            response = null;
        }
        return response;
    }

    public User getUser(String email) {
        if (email != null && !email.isEmpty()) {
            User response;
            try (Session session = DatabaseFactory.getSession()) {
                Query<User> query = session.createQuery("FROM User WHERE EMAIL = ?1", User.class);
                query = query.setParameter(1, email);
                response = query.getSingleResult();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                response = null;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "Email is null or empty");
            return null;
        }
    }

    public User getUser(String email, String password) {
        if ((email != null && password != null) && (!email.isEmpty() && !password.isEmpty())) {
            User response;
            try (Session session = DatabaseFactory.getSession()) {
                Query<User> query = session.createQuery("from User where EMAIL = ?1 AND PASSWORD = ?2", User.class);
                query = query.setParameter(1, email);
                query = query.setParameter(2, password);

                response = query.getSingleResult();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                response = null;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "Email or password is empty or null");
            return null;
        }
    }

    public boolean deleteUser(User user) {
        if (user != null) {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    if (session.load(User.class, user.getId()) != null) {
                        /*
                         * session.createQuery("delete from Blog where user = ?1").setParameter(1,
                         * user).executeUpdate();
                         * 
                         * session.createQuery("delete from Reaction where user = ?1").setParameter(1,
                         * user).executeUpdate();
                         * 
                         * session.createQuery("delete from Comment where user = ?1").setParameter(1,
                         * user).executeUpdate();
                         */

                        session.delete(user);
                        tx.commit();
                        response = Boolean.TRUE;
                    } else {
                        LOGGER.log(Level.INFO, "User not found!");
                        if (tx != null) {
                            if (tx.isActive()) {
                                tx.rollback();
                            }
                        }
                        response = Boolean.FALSE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, ex.getMessage(), ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR,
                        "Process : Delete User from Database | Cause : Exception occured while handling transaction failure. | Loss : Failed to rollback Transaction",
                        ex);
                response = Boolean.FALSE;
            }
            return response;
        } else {
            LOGGER.log(Level.INFO, "User is not available (null)");
            return Boolean.FALSE;
        }
    }

    public boolean checkUsername(String given_username) {
        if (given_username == null || given_username.isEmpty()) {
            return true;
        } else {
            boolean response;
            try (Session session = DatabaseFactory.getSession()) {
                response = session.createSQLQuery("Select * from User where USERNAME = ?1")
                        .setParameter(1, given_username).getResultStream().findAny().isPresent();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.TRUE;
            }
            return response;
        }
    }

}