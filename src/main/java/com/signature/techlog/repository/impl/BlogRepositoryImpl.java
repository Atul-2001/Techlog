package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Blog;
import com.signature.techlog.repository.BlogRepository;

public class BlogRepositoryImpl extends CurdRepositoryImpl<Blog, String> implements BlogRepository {

    private BlogRepositoryImpl(DatabaseFactory databaseFactory) {
        super(Blog.class, databaseFactory);
    }

    public static BlogRepositoryImpl getInstance(DatabaseFactory databaseFactory) {
        return new BlogRepositoryImpl(databaseFactory);
    }

    @Override
    public boolean save(Blog entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveAll(Iterable<Blog> entities) {
        return super.saveAll(entities);
    }

    @Override
    public Blog findById(String id) {
        return super.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    @Override
    public Iterable<Blog> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<Blog> findAllById(Iterable<String> ids) {
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
    public boolean delete(Blog entity) {
        return super.delete(entity);
    }

    @Override
    public boolean deleteAllById(Iterable<String> ids) {
        return super.deleteAllById(ids);
    }

    @Override
    public boolean deleteAll(Iterable<Blog> ids) {
        return super.deleteAll(ids);
    }

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }
}