package com.zjmc.evaluation.service.impl;

import com.zjmc.evaluation.dto.PendingTaskDTO;
import com.zjmc.evaluation.dto.TaskDTO;
import com.zjmc.evaluation.entity.*;
import com.zjmc.evaluation.repository.*;
import com.zjmc.evaluation.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskInstitutionRepository taskInstitutionRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private ScoreRecordRepository scoreRecordRepository;

    @Override
    @Transactional
    public TaskDTO create(TaskDTO dto) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setStatus(0);
        task.setIsCurrent(0);
        task = taskRepository.save(task);

        // 创建任务-机构关联
        if (dto.getInstitutionIds() != null && !dto.getInstitutionIds().isEmpty()) {
            for (Long institutionId : dto.getInstitutionIds()) {
                TaskInstitution ti = new TaskInstitution();
                ti.setTask(task);
                Institution institution = institutionRepository.findById(institutionId)
                    .orElseThrow(() -> new EntityNotFoundException("机构不存在"));
                ti.setInstitution(institution);
                ti.setStatus(0);
                taskInstitutionRepository.save(ti);
            }
        }

        return convertToDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        BeanUtils.copyProperties(dto, task, "id", "createTime", "status");
        task = taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        taskRepository.delete(task);
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        return convertToDTO(task);
    }

    @Override
    public List<TaskDTO> findAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findAllWithData() {
        return findAll();
    }

    @Override
    public TaskDTO getCurrentTask() {
        return taskRepository.findFirstByIsCurrentOrderByCreateTimeDesc(1)
            .map(this::convertToDTO)
            .orElse(null);
    }

    @Override
    @Transactional
    public void setCurrentTask(Long taskId) {
        // 先将所有任务设为非当前
        List<Task> allTasks = taskRepository.findAll();
        for (Task t : allTasks) {
            t.setIsCurrent(0);
        }
        taskRepository.saveAll(allTasks);

        // 设置当前任务
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("任务不存在"));
        task.setIsCurrent(1);
        taskRepository.save(task);
    }

    @Override
    public List<PendingTaskDTO> getPendingTasks(Long judgeId) {
        Judge judge = judgeRepository.findById(judgeId)
            .orElseThrow(() -> new EntityNotFoundException("评委不存在"));

        Task currentTask = taskRepository.findFirstByIsCurrentOrderByCreateTimeDesc(1)
            .orElse(null);

        if (currentTask == null) {
            return new ArrayList<>();
        }

        List<TaskInstitution> taskInstitutions = taskInstitutionRepository.findByTaskId(currentTask.getId());
        List<PendingTaskDTO> result = new ArrayList<>();

        for (TaskInstitution ti : taskInstitutions) {
            Institution targetInstitution = ti.getInstitution();
            
            // 规则1: 同机构回避 - 评委不能给自己的机构打分
            if (judge.getInstitution() != null && 
                judge.getInstitution().getId().equals(targetInstitution.getId())) {
                continue;
            }
            
            // 规则2: 同机构类型派发
            // 专家评委：只评审同类型的机构（如质控中心专家只评审质控中心）
            // 大众评委：只评审同类型的机构
            if (judge.getInstitution() != null) {
                if (judge.getInstitution().getType() != targetInstitution.getType()) {
                    continue;
                }
            } else {
                // 如果评委没有关联机构，则无法判断其专业对口类型，默认不派发任务
                // 这样可以避免因抹去机构信息导致的权限过大问题
                continue;
            }

            PendingTaskDTO dto = new PendingTaskDTO();
            dto.setTaskInstitutionId(ti.getId());
            dto.setTaskId(currentTask.getId());
            dto.setTaskName(currentTask.getName());
            dto.setPeriod(currentTask.getPeriod());
            dto.setInstitutionId(targetInstitution.getId());
            dto.setInstitutionName(targetInstitution.getName());
            dto.setInstitutionType(targetInstitution.getType().name());
            dto.setInstitutionTypeName(getTypeName(targetInstitution.getType()));
            dto.setMaterialDescription(ti.getMaterialDescription());
            dto.setStartDate(currentTask.getStartDate());
            dto.setEndDate(currentTask.getEndDate());

            boolean hasScored = scoreRecordRepository.existsByTaskIdAndInstitutionIdAndJudgeId(
                currentTask.getId(), targetInstitution.getId(), judgeId);
            dto.setHasScored(hasScored);
            dto.setScoreStatus(hasScored ? 1 : 0);

            result.add(dto);
        }

        return result;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        return dto;
    }

    private String getTypeName(Institution.InstitutionType type) {
        return type == Institution.InstitutionType.TECHNICAL_SERVICE ? "技术服务类" : "专业质控中心";
    }

    private List<TaskDTO> getDefaultTasks() {
        List<TaskDTO> list = new ArrayList<>();
        TaskDTO dto = new TaskDTO();
        dto.setId(1L);
        dto.setName("2026年第一季度考核");
        dto.setPeriod("2026Q1");
        dto.setStartDate(LocalDate.of(2026, 1, 1));
        dto.setEndDate(LocalDate.of(2026, 3, 31));
        dto.setDescription("2026年第一季度机构考核评估");
        dto.setIsCurrent(1);
        dto.setStatus(1);
        list.add(dto);

        TaskDTO dto2 = new TaskDTO();
        dto2.setId(2L);
        dto2.setName("2025年第四季度考核");
        dto2.setPeriod("2025Q4");
        dto2.setStartDate(LocalDate.of(2025, 10, 1));
        dto2.setEndDate(LocalDate.of(2025, 12, 31));
        dto2.setDescription("2025年第四季度机构考核评估");
        dto2.setIsCurrent(0);
        dto2.setStatus(2);
        list.add(dto2);

        return list;
    }

    private List<PendingTaskDTO> getDefaultPendingTasks(Judge judge) {
        List<PendingTaskDTO> list = new ArrayList<>();
        String[] institutionNames = {"临床检验中心", "省防盲指导中心", "护理质控中心", "省骨科技术指导中心"};
        String[] types = {"QUALITY_CONTROL", "TECHNICAL_SERVICE", "QUALITY_CONTROL", "TECHNICAL_SERVICE"};

        for (int i = 0; i < institutionNames.length; i++) {
            PendingTaskDTO dto = new PendingTaskDTO();
            dto.setTaskInstitutionId((long) (i + 1));
            dto.setTaskId(1L);
            dto.setTaskName("2026年第一季度考核");
            dto.setPeriod("2026Q1");
            dto.setInstitutionId((long) (i + 100));
            dto.setInstitutionName(institutionNames[i]);
            dto.setInstitutionType(types[i]);
            dto.setInstitutionTypeName(types[i].equals("QUALITY_CONTROL") ? "专业质控中心" : "技术服务类");
            dto.setMaterialDescription("请查看线下派发的考核材料");
            dto.setStartDate(LocalDate.of(2026, 1, 1));
            dto.setEndDate(LocalDate.of(2026, 3, 31));
            dto.setHasScored(i % 2 == 0);
            dto.setScoreStatus(i % 2 == 0 ? 1 : 0);
            list.add(dto);
        }
        return list;
    }
}
