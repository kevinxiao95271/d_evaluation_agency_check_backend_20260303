package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.InstitutionBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionBonusRepository extends JpaRepository<InstitutionBonus, Long> {

    Optional<InstitutionBonus> findByTaskIdAndInstitutionId(Long taskId, Long institutionId);

    List<InstitutionBonus> findByTaskId(Long taskId);

    boolean existsByTaskIdAndInstitutionId(Long taskId, Long institutionId);
}
