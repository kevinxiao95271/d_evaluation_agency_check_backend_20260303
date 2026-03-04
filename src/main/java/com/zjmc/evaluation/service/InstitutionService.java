package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.InstitutionDTO;
import com.zjmc.evaluation.entity.Institution;

import java.util.List;

public interface InstitutionService {

    InstitutionDTO create(InstitutionDTO dto);

    InstitutionDTO update(Long id, InstitutionDTO dto);

    void delete(Long id);

    InstitutionDTO findById(Long id);

    List<InstitutionDTO> findAll();

    List<InstitutionDTO> findByType(Institution.InstitutionType type);

    List<InstitutionDTO> findByTypeWithData(String type);
}
