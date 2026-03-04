package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.InstitutionDTO;
import com.zjmc.evaluation.entity.Institution;
import com.zjmc.evaluation.service.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/institution")
@Tag(name = "机构管理", description = "机构相关接口")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PostMapping
    @Operation(summary = "创建机构", description = "创建新的机构")
    public Result<InstitutionDTO> create(@Valid @RequestBody InstitutionDTO dto) {
        return Result.success(institutionService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新机构", description = "更新机构信息")
    public Result<InstitutionDTO> update(
            @Parameter(description = "机构ID") @PathVariable Long id,
            @Valid @RequestBody InstitutionDTO dto) {
        return Result.success(institutionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除机构", description = "删除机构（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "机构ID") @PathVariable Long id) {
        institutionService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取机构详情", description = "根据ID获取机构详情")
    public Result<InstitutionDTO> findById(@Parameter(description = "机构ID") @PathVariable Long id) {
        return Result.success(institutionService.findById(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有机构", description = "获取所有启用的机构列表")
    public Result<List<InstitutionDTO>> findAll() {
        List<InstitutionDTO> list = institutionService.findAll();
        return Result.success(list);
    }

    @GetMapping("/list/{type}")
    @Operation(summary = "按类型获取机构", description = "根据类型获取机构列表")
    public Result<List<InstitutionDTO>> findByType(
            @Parameter(description = "机构类型：TECHNICAL_SERVICE-技术服务类, QUALITY_CONTROL-专业质控中心") 
            @PathVariable String type) {
        List<InstitutionDTO> list = institutionService.findByTypeWithData(type);
        return Result.success(list);
    }

    @GetMapping("/types")
    @Operation(summary = "获取机构类型", description = "获取所有机构类型")
    public Result<List<String>> getTypes() {
        List<String> types = new ArrayList<>();
        types.add("TECHNICAL_SERVICE");
        types.add("QUALITY_CONTROL");
        return Result.success(types);
    }
}
