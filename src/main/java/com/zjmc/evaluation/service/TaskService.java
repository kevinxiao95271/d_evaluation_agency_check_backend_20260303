package com.zjmc.evaluation.service;

import com.zjmc.evaluation.dto.PendingTaskDTO;
import com.zjmc.evaluation.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    TaskDTO create(TaskDTO dto);

    TaskDTO update(Long id, TaskDTO dto);

    void delete(Long id);

    TaskDTO findById(Long id);

    List<TaskDTO> findAll();

    TaskDTO getCurrentTask();

    void setCurrentTask(Long taskId);

    List<PendingTaskDTO> getPendingTasks(Long judgeId);

    List<TaskDTO> findAllWithData();
}
