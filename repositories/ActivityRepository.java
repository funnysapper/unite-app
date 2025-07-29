package com.Unite.UniteMobileApp.repositories;

import com.Unite.UniteMobileApp.entities.Activity;
import com.Unite.UniteMobileApp.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByUserOrderByTimeDesc(Users user);
}