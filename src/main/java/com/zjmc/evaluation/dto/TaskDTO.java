package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "任务DTO")
public class TaskDTO {

    @Schema(description = "任务ID")
    private Long id;

    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称", required = true)
    private String name;

    @NotBlank(message = "考核周期不能为空")
    @Schema(description = "考核周期，如：2026Q1", required = true)
    private String period;

    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期", required = true)
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    @Schema(description = "结束日期", required = true)
    private LocalDate endDate;

    @Schema(description = "任务描述/材料说明")
    private String description;

    @Schema(description = "是否为当前任务：0-否，1-是")
    private Integer isCurrent;

    @Schema(description = "状态：0-未开始，1-进行中，2-已结束")
    private Integer status;

    @Schema(description = "受评机构ID列表")
    private List<Long> institutionIds;
}
