package com.dms.user.entity;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "dms_txn_api_audit_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "txn_id", length = 50)
    private String txnId;

    @Column(name = "endpoint", length = 255)
    private String endpoint;

    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "client_ip", length = 50)
    private String clientIp;

    @Column(name = "system_ip", length = 50)
    private String systemIp;

    @Column(name = "service_name", length = 100)
    private String serviceName;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}