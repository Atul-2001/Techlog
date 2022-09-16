package com.signature.techlog.repository;

import com.signature.techlog.model.User;

public interface UserRepository extends CurdRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}