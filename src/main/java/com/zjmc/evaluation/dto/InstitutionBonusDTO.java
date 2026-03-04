package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "机构附加项DTO")
public class InstitutionBonusDTO {

    @Schema(description = "ID")
    private Long id;

    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID", required = true)
    private Long taskId;

    @NotNull(message = "机构ID不能为空")
    @Schema(description = "机构ID", required = true)
    private Long institutionId;

    @Schema(description = "是否有主任/副主任到场演讲：0-否，1-是（+8分）")
    private Integer directorPresentation;

    @Schema(description = "是否有人参与会务秘书：0-否，1-是（+2分）")
    private Integer secretaryParticipation;

    @Schema(description = "填写人")
    private String filledBy;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "任务名称")
    private String taskName;
}
