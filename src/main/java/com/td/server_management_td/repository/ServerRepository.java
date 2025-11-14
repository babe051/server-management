package com.td.server_management_td.repository;

import com.td.server_management_td.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    Optional<Server> findByName(String name);
    Optional<Server> findByIpAddress(String ipAddress);
}

