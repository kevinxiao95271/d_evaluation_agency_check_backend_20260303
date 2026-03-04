package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.ScoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreCategoryRepository extends JpaRepository<ScoreCategory, Long> {

    List<ScoreCategory> findByTemplateIdOrderBySortOrderAsc(Long templateId);
}
