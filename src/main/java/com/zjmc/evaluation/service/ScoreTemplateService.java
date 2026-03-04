package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.ScoreCategoryDTO;
import com.zjmc.evaluation.dto.ScoreItemDTO;
import com.zjmc.evaluation.dto.ScoreTemplateDTO;

import java.util.List;

public interface ScoreTemplateService {

    ScoreTemplateDTO create(ScoreTemplateDTO dto);

    ScoreTemplateDTO update(Long id, ScoreTemplateDTO dto);

    void delete(Long id);

    ScoreTemplateDTO findById(Long id);

    List<ScoreTemplateDTO> findAll();

    ScoreTemplateDTO getDefaultTemplate();

    void setDefaultTemplate(Long id);

    ScoreCategoryDTO addCategory(Long templateId, ScoreCategoryDTO dto);

    ScoreItemDTO addItem(Long templateId, Long categoryId, ScoreItemDTO dto);

    List<ScoreTemplateDTO> findAllWithData();
}
