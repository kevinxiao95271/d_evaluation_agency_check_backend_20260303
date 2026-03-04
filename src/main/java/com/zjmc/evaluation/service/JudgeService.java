package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.JudgeDTO;

import java.util.List;

public interface JudgeService {

    JudgeDTO create(JudgeDTO dto);

    JudgeDTO update(Long id, JudgeDTO dto);

    void delete(Long id);

    JudgeDTO findById(Long id);

    List<JudgeDTO> findAll();

    JudgeDTO login(String username, String password);

    List<JudgeDTO> findByType(String type);

    List<JudgeDTO> findAllWithData();
}
