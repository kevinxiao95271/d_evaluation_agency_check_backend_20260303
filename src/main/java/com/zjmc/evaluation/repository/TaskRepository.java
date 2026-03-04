package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusOrderByCreateTimeDesc(Integer status);

    List<Task> findByIsCurrent(Integer isCurrent);

    Optional<Task> findFirstByIsCurrentOrderByCreateTimeDesc(Integer isCurrent);
}
