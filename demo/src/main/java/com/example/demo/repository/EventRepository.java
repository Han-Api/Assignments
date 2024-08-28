package com.example.demo.repository;


import com.example.demo.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Events, String> {

    Optional<List<Events>> findFirst3ByOrderByCostDesc();
    Optional<List<Events>> findFirst3ByOrderByDurationDesc();
}
