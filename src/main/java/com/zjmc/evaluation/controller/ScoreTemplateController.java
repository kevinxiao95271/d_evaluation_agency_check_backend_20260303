package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.ScoreCategoryDTO;
import com.zjmc.evaluation.dto.ScoreItemDTO;
import com.zjmc.evaluation.dto.ScoreTemplateDTO;
import com.zjmc.evaluation.service.ScoreTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/score-template")
@Tag(name = "评分表管理", description = "评分表模板相关接口")
public class ScoreTemplateController {

    @Autowired
    private ScoreTemplateService templateService;

    @PostMapping
    @Operation(summary = "创建模板", description = "创建新的评分表模板")
    public Result<ScoreTemplateDTO> create(@Valid @RequestBody ScoreTemplateDTO dto) {
        return Result.success(templateService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新模板", description = "更新评分表模板")
    public Result<ScoreTemplateDTO> update(
            @Parameter(description = "模板ID") @PathVariable Long id,
            @Valid @RequestBody ScoreTemplateDTO dto) {
        return Result.success(templateService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板", description = "删除评分表模板（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "模板ID") @PathVariable Long id) {
        templateService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取模板详情", description = "根据ID获取模板详情（包含分类和条目）")
    public Result<ScoreTemplateDTO> findById(@Parameter(description = "模板ID") @PathVariable Long id) {
        return Result.success(templateService.findById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有模板", description = "获取所有启用的评分表模板")
    public Result<List<ScoreTemplateDTO>> findAll() {
        List<ScoreTemplateDTO> list = templateService.findAllWithData();
        return Result.success(list);
    }

    @GetMapping("/default")
    @Operation(summary = "获取默认模板", description = "获取当前默认的评分表模板")
    public Result<ScoreTemplateDTO> getDefaultTemplate() {
        return Result.success(templateService.getDefaultTemplate());
    }

    @PostMapping("/{id}/set-default")
    @Operation(summary = "设置默认模板", description = "将指定模板设为默认模板")
    public Result<Void> setDefaultTemplate(@Parameter(description = "模板ID") @PathVariable Long id) {
        templateService.setDefaultTemplate(id);
        return Result.success();
    }

    @PostMapping("/{id}/category")
    @Operation(summary = "添加分类", description = "为模板添加评分分类")
    public Result<ScoreCategoryDTO> addCategory(
            @Parameter(description = "模板ID") @PathVariable Long id,
            @Valid @RequestBody ScoreCategoryDTO dto) {
        return Result.success(templateService.addCategory(id, dto));
    }

    @PostMapping("/{templateId}/category/{categoryId}/item")
    @Operation(summary = "添加评分条目", description = "为分类添加评分条目")
    public Result<ScoreItemDTO> addItem(
            @Parameter(description = "模板ID") @PathVariable Long templateId,
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Valid @RequestBody ScoreItemDTO dto) {
        return Result.success(templateService.addItem(templateId, categoryId, dto));
    }
}
