package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "评分分类DTO")
public class ScoreCategoryDTO {

    @Schema(description = "分类ID")
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", required = true)
    private String name;

    @NotNull(message = "排序号不能为空")
    @Schema(description = "排序号", required = true)
    private Integer sortOrder;

    @Schema(description = "评分条目列表")
    private List<ScoreItemDTO> items;
}
