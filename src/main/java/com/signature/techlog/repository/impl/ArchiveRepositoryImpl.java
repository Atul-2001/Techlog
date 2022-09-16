package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Archive;
import com.signature.techlog.repository.ArchiveRepository;

public class ArchiveRepositoryImpl extends CurdRepositoryImpl<Archive, String> implements ArchiveRepository {

    private ArchiveRepositoryImpl(DatabaseFactory databaseFactory) {
        super(Archive.class, databaseFactory);
    }

    public static ArchiveRepositoryImpl getInstance(DatabaseFactory databaseFactory) {
        return new ArchiveRepositoryImpl(databaseFactory);
    }

    @Override
    public boolean save(Archive entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveAll(Iterable<Archive> entities) {
        return super.saveAll(entities);
    }

    @Override
    public Archive findById(String s) {
        return super.findById(s);
    }

    @Override
    public boolean existsById(String s) {
        return super.existsById(s);
    }

    @Override
    public Iterable<Archive> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<Archive> findAllById(Iterable<String> strings) {
        return super.findAllById(strings);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public boolean deleteById(String s) {
        return super.deleteById(s);
    }

    @Override
    public boolean delete(Archive entity) {
        return super.delete(entity);
    }

    @Override
    public boolean deleteAllById(Iterable<String> strings) {
        return super.deleteAllById(strings);
    }

    @Override
    public boolean deleteAll(Iterable<Archive> entities) {
        return super.deleteAll(entities);
    }

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }
}