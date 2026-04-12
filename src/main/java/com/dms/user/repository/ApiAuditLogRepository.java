package com.dms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.user.entity.ApiAuditLog;

public interface ApiAuditLogRepository extends JpaRepository<ApiAuditLog, Long> {


}
