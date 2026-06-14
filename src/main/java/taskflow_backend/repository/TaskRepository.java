package taskflow_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import taskflow_backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable);



}
