package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "评分记录DTO")
public class ScoreRecordDTO {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "机构ID")
    private Long institutionId;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "评委ID")
    private Long judgeId;

    @Schema(description = "评委姓名")
    private String judgeName;

    @Schema(description = "评委类型")
    private String judgeType;

    @Schema(description = "条目ID")
    private Long itemId;

    @Schema(description = "条目名称")
    private String itemName;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分数")
    private Double score;

    @Schema(description = "满分")
    private Double maxScore;

    @Schema(description = "评语/备注")
    private String comment;

    @Schema(description = "评分时间")
    private LocalDateTime createTime;
}
