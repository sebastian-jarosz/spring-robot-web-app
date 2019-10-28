package com.springrobotwebapp.app.repository;

import com.springrobotwebapp.app.model.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotRepository extends JpaRepository<Robot, Integer> {
}
