package com.td.server_management_td.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import com.td.server_management_td.service.ServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServerController.class)
class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerService serverService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testListServers() throws Exception {
        // Given
        Server server1 = new Server(1L, "Server 1", "192.168.1.1", ServerStatus.STOPPED);
        Server server2 = new Server(2L, "Server 2", "192.168.1.2", ServerStatus.RUNNING);
        List<Server> servers = Arrays.asList(server1, server2);
        when(serverService.listServers()).thenReturn(servers);

        // When & Then
        mockMvc.perform(get("/api/servers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Server 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("RUNNING"));

        verify(serverService, times(1)).listServers();
    }

    @Test
    void testCreateServer() throws Exception {
        // Given
        Server newServer = new Server();
        newServer.setName("New Server");
        newServer.setIpAddress("192.168.1.200");

        Server createdServer = new Server();
        createdServer.setId(1L);
        createdServer.setName("New Server");
        createdServer.setIpAddress("192.168.1.200");
        createdServer.setStatus(ServerStatus.STOPPED);

        when(serverService.createServer(any(Server.class))).thenReturn(createdServer);

        // When & Then
        mockMvc.perform(post("/api/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newServer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Server"))
                .andExpect(jsonPath("$.status").value("STOPPED"));

        verify(serverService, times(1)).createServer(any(Server.class));
    }

    @Test
    void testCreateServerWithValidationError() throws Exception {
        // Given
        Server invalidServer = new Server();
        invalidServer.setName(""); // Empty name should fail validation
        invalidServer.setIpAddress("192.168.1.200");

        // When & Then
        mockMvc.perform(post("/api/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidServer)))
                .andExpect(status().isBadRequest());

        verify(serverService, never()).createServer(any());
    }

    @Test
    void testRenameServer() throws Exception {
        // Given
        String newName = "Renamed Server";
        testServer.setName(newName);
        when(serverService.renameServer(eq(1L), eq(newName))).thenReturn(testServer);

        // When & Then
        mockMvc.perform(put("/api/servers/1/rename")
                        .param("name", newName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(newName));

        verify(serverService, times(1)).renameServer(1L, newName);
    }

    @Test
    void testGetServerStatus() throws Exception {
        // Given
        when(serverService.getServerStatus(1L)).thenReturn(ServerStatus.RUNNING);

        // When & Then
        mockMvc.perform(get("/api/servers/1/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("RUNNING"));

        verify(serverService, times(1)).getServerStatus(1L);
    }

    @Test
    void testStartServer() throws Exception {
        // Given
        testServer.setStatus(ServerStatus.RUNNING);
        when(serverService.startServer(1L)).thenReturn(testServer);

        // When & Then
        mockMvc.perform(put("/api/servers/1/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("RUNNING"));

        verify(serverService, times(1)).startServer(1L);
    }

    @Test
    void testStopServer() throws Exception {
        // Given
        testServer.setStatus(ServerStatus.STOPPED);
        when(serverService.stopServer(1L)).thenReturn(testServer);

        // When & Then
        mockMvc.perform(put("/api/servers/1/stop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("STOPPED"));

        verify(serverService, times(1)).stopServer(1L);
    }

    @Test
    void testDeleteServer() throws Exception {
        // Given
        doNothing().when(serverService).deleteServer(1L);

        // When & Then
        mockMvc.perform(delete("/api/servers/1"))
                .andExpect(status().isNoContent());

        verify(serverService, times(1)).deleteServer(1L);
    }
}

