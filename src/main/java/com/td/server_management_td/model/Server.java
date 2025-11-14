package com.td.server_management_td.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Server name is required")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "IP address is required")
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServerStatus status;
}

