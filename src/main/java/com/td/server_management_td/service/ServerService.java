package com.td.server_management_td.service;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;

import java.util.List;

public interface ServerService {
    List<Server> listServers();
    Server createServer(Server server);
    Server renameServer(Long id, String newName);
    ServerStatus getServerStatus(Long id);
    Server startServer(Long id);
    Server stopServer(Long id);
    void deleteServer(Long id);
}

