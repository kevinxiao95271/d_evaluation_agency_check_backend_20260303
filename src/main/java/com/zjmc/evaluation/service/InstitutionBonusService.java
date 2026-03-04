package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.InstitutionBonusDTO;

import java.util.List;

public interface InstitutionBonusService {

    InstitutionBonusDTO save(InstitutionBonusDTO dto);

    InstitutionBonusDTO findByTaskIdAndInstitutionId(Long taskId, Long institutionId);

    List<InstitutionBonusDTO> findByTaskId(Long taskId);

    List<InstitutionBonusDTO> findByTaskIdWithData(Long taskId);
}
