package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.PendingTaskDTO;
import com.zjmc.evaluation.dto.TaskDTO;
import com.zjmc.evaluation.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@Tag(name = "任务管理", description = "考核任务相关接口")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @Operation(summary = "创建任务", description = "创建新的考核任务")
    public Result<TaskDTO> create(@Valid @RequestBody TaskDTO dto) {
        return Result.success(taskService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新任务", description = "更新任务信息")
    public Result<TaskDTO> update(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @Valid @RequestBody TaskDTO dto) {
        return Result.success(taskService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务", description = "删除任务")
    public Result<Void> delete(@Parameter(description = "任务ID") @PathVariable Long id) {
        taskService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取任务详情", description = "根据ID获取任务详情")
    public Result<TaskDTO> findById(@Parameter(description = "任务ID") @PathVariable Long id) {
        return Result.success(taskService.findById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有任务", description = "获取所有任务列表")
    public Result<List<TaskDTO>> findAll() {
        List<TaskDTO> list = taskService.findAllWithData();
        return Result.success(list);
    }

    @GetMapping("/current")
    @Operation(summary = "获取当前任务", description = "获取当前正在进行的考核任务")
    public Result<TaskDTO> getCurrentTask() {
        TaskDTO task = taskService.getCurrentTask();
        return Result.success(task);
    }

    @PostMapping("/{id}/set-current")
    @Operation(summary = "设置当前任务", description = "将指定任务设为当前任务")
    public Result<Void> setCurrentTask(@Parameter(description = "任务ID") @PathVariable Long id) {
        taskService.setCurrentTask(id);
        return Result.success();
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待评任务", description = "获取评委的待评任务列表")
    public Result<List<PendingTaskDTO>> getPendingTasks(
            @Parameter(description = "评委ID") @RequestParam Long judgeId) {
        List<PendingTaskDTO> list = taskService.getPendingTasks(judgeId);
        return Result.success(list);
    }
}
