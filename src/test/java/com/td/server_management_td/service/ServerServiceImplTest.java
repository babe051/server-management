package com.td.server_management_td.service;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import com.td.server_management_td.repository.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerServiceImplTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerServiceImpl serverService;

    private Server testServer;

    @BeforeEach
    void setUp() {
        testServer = new Server();
        testServer.setId(1L);
        testServer.setName("Test Server");
        testServer.setIpAddress("192.168.1.100");
        testServer.setStatus(ServerStatus.STOPPED);
    }

    @Test
    void testListServers() {
        // Given
        Server server1 = new Server(1L, "Server 1", "192.168.1.1", ServerStatus.STOPPED);
        Server server2 = new Server(2L, "Server 2", "192.168.1.2", ServerStatus.RUNNING);
        List<Server> servers = Arrays.asList(server1, server2);
        when(serverRepository.findAll()).thenReturn(servers);

        // When
        List<Server> result = serverService.listServers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serverRepository, times(1)).findAll();
    }

    @Test
    void testCreateServer() {
        // Given
        Server newServer = new Server();
        newServer.setName("New Server");
        newServer.setIpAddress("192.168.1.200");
        when(serverRepository.save(any(Server.class))).thenAnswer(invocation -> {
            Server server = invocation.getArgument(0);
            server.setId(1L);
            return server;
        });

        // When
        Server result = serverService.createServer(newServer);

        // Then
        assertNotNull(result);
        assertEquals(ServerStatus.STOPPED, result.getStatus());
        assertEquals("New Server", result.getName());
        verify(serverRepository, times(1)).save(any(Server.class));
    }

    @Test
    void testRenameServer() {
        // Given
        String newName = "Renamed Server";
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));
        when(serverRepository.save(any(Server.class))).thenReturn(testServer);

        // When
        Server result = serverService.renameServer(1L, newName);

        // Then
        assertNotNull(result);
        assertEquals(newName, testServer.getName());
        verify(serverRepository, times(1)).findById(1L);
        verify(serverRepository, times(1)).save(testServer);
    }

    @Test
    void testRenameServerNotFound() {
        // Given
        when(serverRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverService.renameServer(999L, "New Name");
        });
        assertTrue(exception.getMessage().contains("Server not found"));
        verify(serverRepository, times(1)).findById(999L);
        verify(serverRepository, never()).save(any());
    }

    @Test
    void testGetServerStatus() {
        // Given
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));

        // When
        ServerStatus status = serverService.getServerStatus(1L);

        // Then
        assertEquals(ServerStatus.STOPPED, status);
        verify(serverRepository, times(1)).findById(1L);
    }

    @Test
    void testGetServerStatusNotFound() {
        // Given
        when(serverRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverService.getServerStatus(999L);
        });
        assertTrue(exception.getMessage().contains("Server not found"));
        verify(serverRepository, times(1)).findById(999L);
    }

    @Test
    void testStartServer() {
        // Given
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));
        when(serverRepository.save(any(Server.class))).thenReturn(testServer);

        // When
        Server result = serverService.startServer(1L);

        // Then
        assertNotNull(result);
        assertEquals(ServerStatus.RUNNING, testServer.getStatus());
        verify(serverRepository, times(1)).findById(1L);
        verify(serverRepository, times(1)).save(testServer);
    }

    @Test
    void testStopServer() {
        // Given
        testServer.setStatus(ServerStatus.RUNNING);
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));
        when(serverRepository.save(any(Server.class))).thenReturn(testServer);

        // When
        Server result = serverService.stopServer(1L);

        // Then
        assertNotNull(result);
        assertEquals(ServerStatus.STOPPED, testServer.getStatus());
        verify(serverRepository, times(1)).findById(1L);
        verify(serverRepository, times(1)).save(testServer);
    }

    @Test
    void testDeleteServerWhenStopped() {
        // Given
        testServer.setStatus(ServerStatus.STOPPED);
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));
        doNothing().when(serverRepository).delete(testServer);

        // When
        assertDoesNotThrow(() -> serverService.deleteServer(1L));

        // Then
        verify(serverRepository, times(1)).findById(1L);
        verify(serverRepository, times(1)).delete(testServer);
    }

    @Test
    void testDeleteServerWhenRunning() {
        // Given
        testServer.setStatus(ServerStatus.RUNNING);
        when(serverRepository.findById(1L)).thenReturn(Optional.of(testServer));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            serverService.deleteServer(1L);
        });
        assertTrue(exception.getMessage().contains("Cannot delete a running server"));
        verify(serverRepository, times(1)).findById(1L);
        verify(serverRepository, never()).delete(any());
    }

    @Test
    void testDeleteServerNotFound() {
        // Given
        when(serverRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serverService.deleteServer(999L);
        });
        assertTrue(exception.getMessage().contains("Server not found"));
        verify(serverRepository, times(1)).findById(999L);
        verify(serverRepository, never()).delete(any());
    }
}

