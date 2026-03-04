package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.InstitutionBonusDTO;
import com.zjmc.evaluation.service.InstitutionBonusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bonus")
@Tag(name = "附加项管理", description = "机构附加项（主任演讲、会务秘书）相关接口")
public class InstitutionBonusController {

    @Autowired
    private InstitutionBonusService bonusService;

    @PostMapping
    @Operation(summary = "保存附加项", description = "保存或更新机构的附加项信息")
    public Result<InstitutionBonusDTO> save(@Valid @RequestBody InstitutionBonusDTO dto) {
        return Result.success(bonusService.save(dto));
    }

    @GetMapping
    @Operation(summary = "获取附加项", description = "获取指定任务和机构的附加项信息")
    public Result<InstitutionBonusDTO> findByTaskIdAndInstitutionId(
            @Parameter(description = "任务ID") @RequestParam Long taskId,
            @Parameter(description = "机构ID") @RequestParam Long institutionId) {
        return Result.success(bonusService.findByTaskIdAndInstitutionId(taskId, institutionId));
    }

    @GetMapping("/list/{taskId}")
    @Operation(summary = "获取任务所有附加项", description = "获取指定任务下所有机构的附加项列表")
    public Result<List<InstitutionBonusDTO>> findByTaskId(
            @Parameter(description = "任务ID") @PathVariable Long taskId) {
        List<InstitutionBonusDTO> list = bonusService.findByTaskIdWithData(taskId);
        return Result.success(list);
    }
}
