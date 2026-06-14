package taskflow_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskflow_backend.audit.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
}