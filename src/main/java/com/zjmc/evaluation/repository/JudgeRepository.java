package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, Long> {

    Optional<Judge> findByUsernameAndStatus(String username, Integer status);

    List<Judge> findByTypeAndStatus(Judge.JudgeType type, Integer status);

    List<Judge> findByStatus(Integer status);

    List<Judge> findByInstitutionIdAndTypeAndStatus(Long institutionId, Judge.JudgeType type, Integer status);
}
