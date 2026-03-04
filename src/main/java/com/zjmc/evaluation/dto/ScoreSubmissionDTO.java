package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "评分提交记录DTO")
public class ScoreSubmissionDTO {

    @Schema(description = "提交ID(使用第一条记录的ID)")
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

    @Schema(description = "评分模式: ITEM-逐条打分, TOTAL-直接打总分")
    private String scoreMode;

    @Schema(description = "总分")
    private Double totalScore;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "评分条目明细")
    private List<ItemScoreDetail> itemScores;

    @Data
    @Schema(description = "评分条目明细")
    public static class ItemScoreDetail {
        
        @Schema(description = "记录ID")
        private Long id;
        
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
    }
}
