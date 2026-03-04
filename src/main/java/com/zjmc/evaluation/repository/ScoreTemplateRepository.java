package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.ScoreTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreTemplateRepository extends JpaRepository<ScoreTemplate, Long> {

    List<ScoreTemplate> findByStatus(Integer status);

    Optional<ScoreTemplate> findByIsDefaultAndStatus(Integer isDefault, Integer status);
}
