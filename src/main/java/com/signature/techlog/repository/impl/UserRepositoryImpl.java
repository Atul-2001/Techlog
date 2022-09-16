package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.User;
import com.signature.techlog.repository.UserRepository;
import org.apache.logging.log4j.Level;
import org.hibernate.Session;

public class UserRepositoryImpl extends CurdRepositoryImpl<User, String> implements UserRepository {

    private UserRepositoryImpl(DatabaseFactory databaseFactory) {
        super(User.class, databaseFactory);
    }

    public static UserRepositoryImpl getInstance(DatabaseFactory databaseFactory) {
        return new UserRepositoryImpl(databaseFactory);
    }

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveAll(Iterable<User> entities) {
        return super.saveAll(entities);
    }

    @Override
    public User findById(String id) {
        return super.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        } else {
            User response;
            try (Session session = super.database.getSession()) {
                response = session.createQuery("from User u where u.email = :email", User.class)
                        .setParameter("email", email)
                        .getResultList()
                        .stream()
                        .findAny().orElseThrow();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = null;
            }
            return response;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        } else {
            boolean response;
            try (Session session = super.database.getSession()) {
                response = session.createQuery("from User u where u.email = :email", User.class)
                        .setParameter("email", email)
                        .getResultList()
                        .stream()
                        .findAny().isPresent();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return true;
        } else {
            boolean response;
            try (Session session = super.database.getSession()) {
                response = session.createQuery("from User u where u.username = ?1", User.class)
                        .setParameter(1, username).getResultStream().findAny().isPresent();
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.TRUE;
            }
            return response;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<String> ids) {
        return super.findAllById(ids);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public boolean deleteById(String id) {
        return super.deleteById(id);
    }

    @Override
    public boolean delete(User entity) {
        return super.delete(entity);
    }

    @Override
    public boolean deleteAllById(Iterable<String> ids) {
        return super.deleteAllById(ids);
    }

    @Override
    public boolean deleteAll(Iterable<User> entities) {
        return super.deleteAll(entities);
    }

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }
}