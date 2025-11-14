package com.td.server_management_td.repository;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServerRepository serverRepository;

    private Server testServer;

    @BeforeEach
    void setUp() {
        testServer = new Server();
        testServer.setName("Test Server");
        testServer.setIpAddress("192.168.1.100");
        testServer.setStatus(ServerStatus.STOPPED);
    }

    @Test
    void testSaveServer() {
        // When
        Server saved = serverRepository.save(testServer);

        // Then
        assertNotNull(saved.getId());
        assertEquals("Test Server", saved.getName());
        assertEquals("192.168.1.100", saved.getIpAddress());
        assertEquals(ServerStatus.STOPPED, saved.getStatus());
    }

    @Test
    void testFindById() {
        // Given
        Server saved = entityManager.persistAndFlush(testServer);

        // When
        Optional<Server> found = serverRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Server", found.get().getName());
    }

    @Test
    void testFindByName() {
        // Given
        Server saved = entityManager.persistAndFlush(testServer);

        // When
        Optional<Server> found = serverRepository.findByName("Test Server");

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Server", found.get().getName());
    }

    @Test
    void testFindByNameNotFound() {
        // When
        Optional<Server> found = serverRepository.findByName("Non Existent");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByIpAddress() {
        // Given
        Server saved = entityManager.persistAndFlush(testServer);

        // When
        Optional<Server> found = serverRepository.findByIpAddress("192.168.1.100");

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("192.168.1.100", found.get().getIpAddress());
    }

    @Test
    void testFindAll() {
        // Given
        Server server1 = new Server(null, "Server 1", "192.168.1.1", ServerStatus.STOPPED);
        Server server2 = new Server(null, "Server 2", "192.168.1.2", ServerStatus.RUNNING);
        entityManager.persistAndFlush(server1);
        entityManager.persistAndFlush(server2);

        // When
        List<Server> servers = serverRepository.findAll();

        // Then
        assertEquals(2, servers.size());
    }

    @Test
    void testDeleteServer() {
        // Given
        Server saved = entityManager.persistAndFlush(testServer);
        Long id = saved.getId();

        // When
        serverRepository.delete(saved);
        entityManager.flush();

        // Then
        Optional<Server> found = serverRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateServer() {
        // Given
        Server saved = entityManager.persistAndFlush(testServer);
        saved.setName("Updated Name");
        saved.setStatus(ServerStatus.RUNNING);

        // When
        Server updated = serverRepository.save(saved);

        // Then
        assertEquals("Updated Name", updated.getName());
        assertEquals(ServerStatus.RUNNING, updated.getStatus());
    }
}

