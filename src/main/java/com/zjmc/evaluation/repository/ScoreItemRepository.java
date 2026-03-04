package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.ScoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreItemRepository extends JpaRepository<ScoreItem, Long> {

    List<ScoreItem> findByTemplateIdAndStatusOrderBySortOrderAsc(Long templateId, Integer status);

    List<ScoreItem> findByCategoryIdAndStatusOrderBySortOrderAsc(Long categoryId, Integer status);
}
