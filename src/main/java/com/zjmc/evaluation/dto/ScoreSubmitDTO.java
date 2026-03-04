package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "评分提交DTO")
public class ScoreSubmitDTO {

    @NotNull(message = "任务ID不能为空")
    @Schema(description = "任务ID", required = true)
    private Long taskId;

    @NotNull(message = "机构ID不能为空")
    @Schema(description = "机构ID", required = true)
    private Long institutionId;

    @NotNull(message = "评委ID不能为空")
    @Schema(description = "评委ID", required = true)
    private Long judgeId;

    @Schema(description = "评分模式：ITEM-逐条打分, TOTAL-直接打总分")
    private String scoreMode;

    @Schema(description = "直接打总分（当scoreMode=TOTAL时使用）")
    private Double totalScore;

    @Schema(description = "逐条评分列表（当scoreMode=ITEM时使用）")
    private List<ItemScoreDTO> itemScores;

    @Data
    @Schema(description = "条目评分DTO")
    public static class ItemScoreDTO {

        @NotNull(message = "条目ID不能为空")
        @Schema(description = "条目ID", required = true)
        private Long itemId;

        @NotNull(message = "评分不能为空")
        @Schema(description = "评分分数", required = true)
        private Double score;

        @Schema(description = "评语/备注")
        private String comment;
    }
}
