package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Archive;
import com.signature.techlog.model.Blog;
import com.signature.techlog.model.Comment;
import com.signature.techlog.model.Reaction;
import com.signature.techlog.model.User;
import com.signature.techlog.model.base.BaseEntity;
import com.signature.techlog.repository.CurdRepository;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.Level;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CurdRepositoryImpl<T extends BaseEntity, ID> implements CurdRepository<T, ID> {

    private final Class<T> domainClass;
    protected final DatabaseFactory database;

    public CurdRepositoryImpl(Class<T> domainClass, DatabaseFactory database) {
        this.domainClass = domainClass;
        this.database = database;
    }

    @Override
    public boolean save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.save(entity);
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean saveAll(Iterable<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Entities cannot be null.");
        } else {
            boolean response;
            try (StatelessSession session = database.getFactory().openStatelessSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    entities.forEach(entity -> {
                        if (entity != null) {
                            session.insert(entity);
                        } else {
                            throw new IllegalArgumentException("Entities should not contain null entity.");
                        }
                    });
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public T findById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        } else {
            T response;
            try (Session session = database.getSession()) {
                response = session.get(domainClass, (Serializable) id);
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = null;
            }
            return response;
        }
    }

    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                response = session.get(domainClass, (Serializable) id) == null ? Boolean.FALSE : Boolean.TRUE;
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public Iterable<T> findAll() {
        Iterable<T> entities;
        try (Session session = database.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(domainClass);

            Root<T> rootEntry = criteriaQuery.from(domainClass);
            CriteriaQuery<T> all = criteriaQuery.select(rootEntry);

            TypedQuery<T> allQuery = session.createQuery(all);
            entities = allQuery.getResultList();
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "", ex);
            entities = null;
        }
        return entities;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("IDs cannot be null.");
        } else {
            List<ID> idList = new ArrayList<>();
            ids.forEach(id -> {
                if (id == null) {
                    throw new IllegalArgumentException("IDs should not contain a null id.");
                } else {
                    idList.add(id);
                }
            });

            List<T> entities = new ArrayList<>();
            try (Session session = database.getSession()) {
                MultiIdentifierLoadAccess<T> multiLoadAccess = session.byMultipleIds(domainClass)
                        .enableSessionCheck(Boolean.TRUE)
                        .withBatchSize(1_000);
                entities.addAll(multiLoadAccess.multiLoad(idList.toArray()));
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
            }
            return entities;
        }
    }

    @Override
    public long count() {
        long count;
        try (Session session = database.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

            Root<T> rootEntry = criteriaQuery.from(domainClass);
            criteriaQuery = criteriaQuery.select(criteriaBuilder.count(rootEntry));

            TypedQuery<Long> countQuery = session.createQuery(criteriaQuery);
            count = countQuery.getSingleResult();
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "", ex);
            count = -1;
        }
        return count;
    }

    @Override
    public boolean deleteById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    try {
                        T entity = session.load(domainClass, (Serializable) id);
                        session.delete(entity);
                    } catch (ObjectNotFoundException ex) {
                        T entity = session.get(domainClass, (Serializable) id);
                        if (entity != null) {
                            session.delete(entity);
                        } else {
                            throw ex;
                        }
                    }
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    try {
                        T loaded = session.load(domainClass, entity.getId());
                        session.delete(loaded);
                    } catch (ObjectNotFoundException ex) {
                        T loaded = session.get(domainClass, entity.getId());
                        if (loaded != null) {
                            session.delete(loaded);
                        } else {
                            throw ex;
                        }
                    }
                    tx.commit();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (PersistenceException | IllegalStateException ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean deleteAllById(Iterable<ID> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("IDs cannot be null.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                Transaction tx = null;
                try {
                    session.flush();
                    session.clear();
                    tx = session.beginTransaction();
                    ids.forEach(id -> {
                        if (id == null) {
                            throw new IllegalArgumentException("IDs should not contain a null id.");
                        } else {
                            try {
                                T entity = session.load(domainClass, (Serializable) id);
                                session.delete(entity);
                            } catch (ObjectNotFoundException ex) {
                                T entity = session.get(domainClass, (Serializable) id);
                                if (entity != null) {
                                    session.delete(entity);
                                } else {
                                    throw ex;
                                }
                            }
                        }
                    });
                    tx.commit();
                    session.flush();
                    session.clear();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean deleteAll(Iterable<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("IDs cannot be null.");
        } else {
            boolean response;
            try (Session session = database.getSession()) {
                Transaction tx = null;
                try {
                    session.flush();
                    session.clear();
                    tx = session.beginTransaction();
                    entities.forEach(entity -> {
                        if (entity == null) {
                            throw new IllegalArgumentException("IDs should not contain a null id.");
                        } else {
                            try {
                                T loaded = session.load(domainClass, entity.getId());
                                session.delete(loaded);
                            } catch (ObjectNotFoundException ex) {
                                T loaded = session.get(domainClass, entity.getId());
                                if (loaded != null) {
                                    session.delete(loaded);
                                } else {
                                    throw ex;
                                }
                            }
                        }
                    });
                    tx.commit();
                    session.flush();
                    session.clear();
                    response = Boolean.TRUE;
                } catch (Exception ex) {
                    LOGGER.log(Level.ERROR, "", ex);
                    if (tx != null) {
                        if (tx.isActive()) {
                            tx.rollback();
                        }
                    }
                    response = Boolean.FALSE;
                }
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                response = Boolean.FALSE;
            }
            return response;
        }
    }

    @Override
    public boolean deleteAll() {
        boolean response;
        try (Session session = database.getSession()) {
            Transaction tx = null;
            try {
                session.flush();
                session.clear();
                tx = session.beginTransaction();
                if (domainClass.isAssignableFrom(Archive.class)) {
                    session.createQuery("delete from Archive").executeUpdate();
                } else if (domainClass.isAssignableFrom(Comment.class)) {
                    session.createQuery("delete from Comment").executeUpdate();
                } else if (domainClass.isAssignableFrom(Reaction.class)) {
                    session.createQuery("delete from Reaction").executeUpdate();
                } else if (domainClass.isAssignableFrom(Blog.class)) {
                    session.createQuery("delete from Comment").executeUpdate();
                    session.createQuery("delete from Reaction").executeUpdate();
                    session.createQuery("delete from Blog").executeUpdate();
                } else if (domainClass.isAssignableFrom(User.class)) {
                    session.createQuery("delete from Archive").executeUpdate();
                    session.createQuery("delete from Comment").executeUpdate();
                    session.createQuery("delete from Reaction").executeUpdate();
                    session.createQuery("delete from Blog").executeUpdate();
                    session.createQuery("delete from User").executeUpdate();
                }
                tx.commit();
                session.flush();
                session.clear();
                response = Boolean.TRUE;
            } catch (Exception ex) {
                LOGGER.log(Level.ERROR, "", ex);
                if (tx != null) {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                }
                response = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "", ex);
            response = Boolean.FALSE;
        }
        return response;
    }
}