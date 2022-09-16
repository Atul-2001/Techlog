package com.signature.techlog.repository.impl;

import com.signature.techlog.database.DatabaseFactory;
import com.signature.techlog.model.Comment;
import com.signature.techlog.repository.CommentRepository;

public class CommentRepositoryImpl extends CurdRepositoryImpl<Comment, String> implements CommentRepository {
    
    private CommentRepositoryImpl(DatabaseFactory databaseFactory) {
        super(Comment.class, databaseFactory);
    }

    public static CommentRepositoryImpl getInstance(DatabaseFactory databaseFactory) {
        return new CommentRepositoryImpl(databaseFactory);
    }
    
    @Override
    public boolean save(Comment entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveAll(Iterable<Comment> entities) {
        return super.saveAll(entities);
    }

    @Override
    public Comment findById(String id) {
        return super.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    @Override
    public Iterable<Comment> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<Comment> findAllById(Iterable<String> ids) {
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
    public boolean delete(Comment entity) {
        return super.delete(entity);
    }

    @Override
    public boolean deleteAllById(Iterable<String> ids) {
        return super.deleteAllById(ids);
    }

    @Override
    public boolean deleteAll(Iterable<Comment> entities) {
        return super.deleteAll(entities);
    }

    @Override
    public boolean deleteAll() {
        return super.deleteAll();
    }
}