package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.TaskInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskInstitutionRepository extends JpaRepository<TaskInstitution, Long> {

    List<TaskInstitution> findByTaskId(Long taskId);

    List<TaskInstitution> findByInstitutionId(Long institutionId);

    Optional<TaskInstitution> findByTaskIdAndInstitutionId(Long taskId, Long institutionId);

    boolean existsByTaskIdAndInstitutionId(Long taskId, Long institutionId);
}
