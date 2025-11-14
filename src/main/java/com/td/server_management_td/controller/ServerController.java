package com.td.server_management_td.controller;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import com.td.server_management_td.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
@Tag(name = "Server Management", description = "API for managing servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    @Operation(summary = "List all servers", description = "Retrieve a list of all servers")
    public ResponseEntity<List<Server>> listServers() {
        return ResponseEntity.ok(serverService.listServers());
    }

    @PostMapping
    @Operation(summary = "Create a new server", description = "Create a new server with STOPPED status")
    public ResponseEntity<Server> createServer(@Valid @RequestBody Server server) {
        Server created = serverService.createServer(server);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/rename")
    @Operation(summary = "Rename a server", description = "Update the name of an existing server")
    public ResponseEntity<Server> renameServer(
            @PathVariable Long id,
            @RequestParam String name) {
        Server updated = serverService.renameServer(id, name);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get server status", description = "Retrieve the current status of a server")
    public ResponseEntity<ServerStatusResponse> getServerStatus(@PathVariable Long id) {
        ServerStatus status = serverService.getServerStatus(id);
        return ResponseEntity.ok(new ServerStatusResponse(status));
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Start a server", description = "Change server status to RUNNING")
    public ResponseEntity<Server> startServer(@PathVariable Long id) {
        Server updated = serverService.startServer(id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/stop")
    @Operation(summary = "Stop a server", description = "Change server status to STOPPED")
    public ResponseEntity<Server> stopServer(@PathVariable Long id) {
        Server updated = serverService.stopServer(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a server", description = "Delete a server. Only allowed if status is STOPPED")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }

    // DTO for status response
    public record ServerStatusResponse(ServerStatus status) {}
}

