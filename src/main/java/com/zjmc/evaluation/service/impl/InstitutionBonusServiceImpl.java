package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.InstitutionBonusDTO;
import com.zjmc.evaluation.entity.Institution;
import com.zjmc.evaluation.entity.InstitutionBonus;
import com.zjmc.evaluation.entity.Task;
import com.zjmc.evaluation.repository.InstitutionBonusRepository;
import com.zjmc.evaluation.repository.InstitutionRepository;
import com.zjmc.evaluation.repository.TaskRepository;
import com.zjmc.evaluation.service.InstitutionBonusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionBonusServiceImpl implements InstitutionBonusService {

    @Autowired
    private InstitutionBonusRepository bonusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Override
    @Transactional
    public InstitutionBonusDTO save(InstitutionBonusDTO dto) {
        Task task = taskRepository.findById(dto.getTaskId())
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        Institution institution = institutionRepository.findById(dto.getInstitutionId())
            .orElseThrow(() -> new EntityNotFoundException("机构不存在"));

        InstitutionBonus bonus = bonusRepository.findByTaskIdAndInstitutionId(dto.getTaskId(), dto.getInstitutionId())
            .orElse(new InstitutionBonus());

        bonus.setTask(task);
        bonus.setInstitution(institution);
        // 如果传入 null，默认为 0（未勾选），需要前端明确传 1 才表示勾选
        bonus.setDirectorPresentation(dto.getDirectorPresentation() != null ? dto.getDirectorPresentation() : 0);
        bonus.setSecretaryParticipation(dto.getSecretaryParticipation() != null ? dto.getSecretaryParticipation() : 0);
        bonus.setFilledBy(dto.getFilledBy());
        bonus.setFilledAt(LocalDateTime.now());

        bonus = bonusRepository.save(bonus);
        return convertToDTO(bonus);
    }

    @Override
    public InstitutionBonusDTO findByTaskIdAndInstitutionId(Long taskId, Long institutionId) {
        InstitutionBonus bonus = bonusRepository.findByTaskIdAndInstitutionId(taskId, institutionId)
            .orElseGet(() -> {
                // 如果没有记录，返回空对象（值为 0，表示未勾选）
                InstitutionBonus empty = new InstitutionBonus();
                empty.setDirectorPresentation(0);
                empty.setSecretaryParticipation(0);
                return empty;
            });
        return convertToDTO(bonus);
    }

    @Override
    public List<InstitutionBonusDTO> findByTaskId(Long taskId) {
        List<InstitutionBonus> bonuses = bonusRepository.findByTaskId(taskId);
        return bonuses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<InstitutionBonusDTO> findByTaskIdWithData(Long taskId) {
        // 直接返回数据库中的数据，不添加默认值
        return findByTaskId(taskId);
    }

    private InstitutionBonusDTO convertToDTO(InstitutionBonus bonus) {
        InstitutionBonusDTO dto = new InstitutionBonusDTO();
        BeanUtils.copyProperties(bonus, dto);
        if (bonus.getTask() != null) {
            dto.setTaskId(bonus.getTask().getId());
            dto.setTaskName(bonus.getTask().getName());
        }
        if (bonus.getInstitution() != null) {
            dto.setInstitutionId(bonus.getInstitution().getId());
            dto.setInstitutionName(bonus.getInstitution().getName());
        }
        // 确保字段不为 null
        if (dto.getDirectorPresentation() == null) {
            dto.setDirectorPresentation(0);
        }
        if (dto.getSecretaryParticipation() == null) {
            dto.setSecretaryParticipation(0);
        }
        return dto;
    }

    // 删除不再使用的 getDefaultBonuses 方法
}
