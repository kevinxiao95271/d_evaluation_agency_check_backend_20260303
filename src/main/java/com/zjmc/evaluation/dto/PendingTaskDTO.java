package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "待评任务DTO")
public class PendingTaskDTO {

    @Schema(description = "任务机构关联ID")
    private Long taskInstitutionId;

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "考核周期")
    private String period;

    @Schema(description = "机构ID")
    private Long institutionId;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "机构类型")
    private String institutionType;

    @Schema(description = "机构类型名称")
    private String institutionTypeName;

    @Schema(description = "材料说明")
    private String materialDescription;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "是否已评分")
    private Boolean hasScored;

    @Schema(description = "评分状态：0-未评分，1-已评分")
    private Integer scoreStatus;
}
