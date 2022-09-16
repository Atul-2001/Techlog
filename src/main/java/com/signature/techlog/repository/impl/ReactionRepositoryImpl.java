package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Reaction;
import com.signature.techlog.repository.ReactionRepository;

public class ReactionRepositoryImpl extends CurdRepositoryImpl<Reaction, String> implements ReactionRepository {

    private ReactionRepositoryImpl(DatabaseFactory databaseFactory) {
        super(Reaction.class, databaseFactory);
    }
    
    public static ReactionRepositoryImpl getInstance(DatabaseFactory databaseFactory) {
        return new ReactionRepositoryImpl(databaseFactory);
    }

    @Override
    public boolean save(Reaction entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveAll(Iterable<Reaction> entities) {
        return super.saveAll(entities);
    }

    @Override
    public Reaction findById(String id) {
        return super.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    @Override
    public Iterable<Reaction> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<Reaction> findAllById(Iterable<String> ids) {
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
    public boolean delete(Reaction entity) {
        return super.delete(entity);
    }

    @Override
    public boolean deleteAllById(Iterable<String> ids) {
        return super.deleteAllById(ids);
    }

    @Override
    public boolean deleteAll(Iterable<Reaction> entities) {
        return super.deleteAll(entities);
    }

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }
}