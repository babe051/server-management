package com.td.server_management_td.service;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import com.td.server_management_td.repository.ServerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Server> listServers() {
        return serverRepository.findAll();
    }

    @Override
    public Server createServer(Server server) {
        server.setStatus(ServerStatus.STOPPED);
        return serverRepository.save(server);
    }

    @Override
    public Server renameServer(Long id, String newName) {
        Server server = getServerOrThrow(id);
        server.setName(newName);
        return serverRepository.save(server);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerStatus getServerStatus(Long id) {
        Server server = getServerOrThrow(id);
        return server.getStatus();
    }

    @Override
    public Server startServer(Long id) {
        Server server = getServerOrThrow(id);
        server.setStatus(ServerStatus.RUNNING);
        return serverRepository.save(server);
    }

    @Override
    public Server stopServer(Long id) {
        Server server = getServerOrThrow(id);
        server.setStatus(ServerStatus.STOPPED);
        return serverRepository.save(server);
    }

    @Override
    public void deleteServer(Long id) {
        Server server = getServerOrThrow(id);
        if (server.getStatus() == ServerStatus.RUNNING) {
            throw new IllegalStateException("Cannot delete a running server. Stop the server first.");
        }
        serverRepository.delete(server);
    }

    private Server getServerOrThrow(Long id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found with id: " + id));
    }
}

