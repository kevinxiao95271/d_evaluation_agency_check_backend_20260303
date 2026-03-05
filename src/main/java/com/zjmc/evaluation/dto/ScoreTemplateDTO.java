package com.zjmc.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "评分模板DTO")
public class ScoreTemplateDTO {

    @Schema(description = "模板ID")
    private Long id;

    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称", required = true)
    private String name;

    @NotNull(message = "评分表满分值不能为空")
    @Schema(description = "评分表满分值（如 100 分）", required = true)
    @JsonFormat(pattern = "0.00")
    private Double templateMaxScore;

    @NotNull(message = "系统总分值不能为空")
    @Schema(description = "系统总分值（如 45 分）", required = true)
    @JsonFormat(pattern = "0.00")
    private Double systemTotalScore;

    @Schema(description = "是否默认模板：0-否，1-是")
    private Integer isDefault;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "评分分类列表")
    private List<ScoreCategoryDTO> categories;
}
