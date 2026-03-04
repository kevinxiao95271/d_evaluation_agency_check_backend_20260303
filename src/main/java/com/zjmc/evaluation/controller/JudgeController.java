package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.JudgeDTO;
import com.zjmc.evaluation.service.JudgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/judge")
@Tag(name = "评委管理", description = "评委相关接口")
public class JudgeController {

    @Autowired
    private JudgeService judgeService;

    @PostMapping
    @Operation(summary = "创建评委", description = "创建新的评委")
    public Result<JudgeDTO> create(@Valid @RequestBody JudgeDTO dto) {
        return Result.success(judgeService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新评委", description = "更新评委信息")
    public Result<JudgeDTO> update(
            @Parameter(description = "评委ID") @PathVariable Long id,
            @Valid @RequestBody JudgeDTO dto) {
        return Result.success(judgeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评委", description = "删除评委（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "评委ID") @PathVariable Long id) {
        judgeService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评委详情", description = "根据ID获取评委详情")
    public Result<JudgeDTO> findById(@Parameter(description = "评委ID") @PathVariable Long id) {
        return Result.success(judgeService.findById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有评委", description = "获取所有启用的评委列表")
    public Result<List<JudgeDTO>> findAll() {
        List<JudgeDTO> list = judgeService.findAllWithData();
        return Result.success(list);
    }

    @GetMapping("/list/{type}")
    @Operation(summary = "按类型获取评委", description = "根据类型获取评委列表")
    public Result<List<JudgeDTO>> findByType(
            @Parameter(description = "评委类型：EXPERT-专家评委, PUBLIC-大众评委") 
            @PathVariable String type) {
        return Result.success(judgeService.findByType(type));
    }

    @PostMapping("/login")
    @Operation(summary = "评委登录", description = "评委登录接口")
    public Result<JudgeDTO> login(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "密码") @RequestParam String password) {
        return Result.success(judgeService.login(username, password));
    }

    @GetMapping("/types")
    @Operation(summary = "获取评委类型", description = "获取所有评委类型")
    public Result<List<String>> getTypes() {
        List<String> types = new ArrayList<>();
        types.add("EXPERT");
        types.add("PUBLIC");
        return Result.success(types);
    }
}
