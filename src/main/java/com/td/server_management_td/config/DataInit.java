package com.td.server_management_td.config;

import com.td.server_management_td.model.Server;
import com.td.server_management_td.model.ServerStatus;
import com.td.server_management_td.repository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInit {

    @Bean
    CommandLineRunner initDatabase(ServerRepository serverRepository) {
        return args -> {
            if (serverRepository.count() == 0) {
                Server server1 = new Server();
                server1.setName("Web Server 1");
                server1.setIpAddress("192.168.1.10");
                server1.setStatus(ServerStatus.STOPPED);
                serverRepository.save(server1);

                Server server2 = new Server();
                server2.setName("Database Server");
                server2.setIpAddress("192.168.1.20");
                server2.setStatus(ServerStatus.STOPPED);
                serverRepository.save(server2);

                Server server3 = new Server();
                server3.setName("Application Server");
                server3.setIpAddress("192.168.1.30");
                server3.setStatus(ServerStatus.STOPPED);
                serverRepository.save(server3);
            }
        };
    }
}

