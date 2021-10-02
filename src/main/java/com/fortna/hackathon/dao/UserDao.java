package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
    
    @Modifying
    @Query("update User u set u.accessToken = :accessToken where u.username = :username")
    void updateAccessTokenByUsername(@Param(value = "accessToken") String token, @Param(value = "username") String username);
    
    List<User> findAllByOrderByIdAsc();
}