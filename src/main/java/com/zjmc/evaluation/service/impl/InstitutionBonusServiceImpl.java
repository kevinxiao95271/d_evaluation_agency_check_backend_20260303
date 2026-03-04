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
                // 返回空对象
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
        List<InstitutionBonusDTO> bonuses = findByTaskId(taskId);
        if (bonuses.isEmpty()) {
            return getDefaultBonuses(taskId);
        }
        return bonuses;
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
        return dto;
    }

    private List<InstitutionBonusDTO> getDefaultBonuses(Long taskId) {
        List<InstitutionBonusDTO> list = new ArrayList<>();
        String[][] data = {
            {"1", "临床检验中心", "1", "1"},
            {"2", "护理质控中心", "0", "1"},
            {"3", "省防盲指导中心", "1", "0"},
            {"4", "省骨科技术指导中心", "0", "0"},
        };

        for (String[] item : data) {
            InstitutionBonusDTO dto = new InstitutionBonusDTO();
            dto.setId((long) (item[0].hashCode()));
            dto.setTaskId(taskId);
            dto.setInstitutionId(Long.parseLong(item[0]));
            dto.setInstitutionName(item[1]);
            dto.setDirectorPresentation(Integer.parseInt(item[2]));
            dto.setSecretaryParticipation(Integer.parseInt(item[3]));
            dto.setFilledBy("管理员");
            list.add(dto);
        }
        return list;
    }
}
