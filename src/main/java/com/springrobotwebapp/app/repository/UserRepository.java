package com.springrobotwebapp.app.repository;

import com.springrobotwebapp.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
