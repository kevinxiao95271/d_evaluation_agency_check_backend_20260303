package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "机构DTO")
public class InstitutionDTO {

    @Schema(description = "机构ID")
    private Long id;

    @NotBlank(message = "机构名称不能为空")
    @Schema(description = "机构名称", required = true)
    private String name;

    @NotNull(message = "机构类型不能为空")
    @Schema(description = "机构类型：TECHNICAL_SERVICE-技术服务类, QUALITY_CONTROL-专业质控中心", required = true)
    private String type;

    @Schema(description = "机构描述")
    private String description;

    @Schema(description = "负责人")
    private String director;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
}
