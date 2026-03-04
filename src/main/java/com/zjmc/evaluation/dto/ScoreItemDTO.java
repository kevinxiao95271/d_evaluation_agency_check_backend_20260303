package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "评分条目DTO")
public class ScoreItemDTO {

    @Schema(description = "条目ID")
    private Long id;

    @NotBlank(message = "条目名称不能为空")
    @Schema(description = "条目名称/评分标准", required = true)
    private String name;

    @Schema(description = "评分细则说明")
    private String description;

    @NotNull(message = "条目满分值不能为空")
    @Schema(description = "条目满分值", required = true)
    private Double maxScore;

    @NotNull(message = "权重不能为空")
    @Schema(description = "权重（用于总分自动分配）", required = true)
    private Double weight;

    @NotNull(message = "排序号不能为空")
    @Schema(description = "排序号", required = true)
    private Integer sortOrder;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
}
