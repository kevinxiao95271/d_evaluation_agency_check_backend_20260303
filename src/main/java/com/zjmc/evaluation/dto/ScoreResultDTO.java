package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "评分结果DTO")
public class ScoreResultDTO {

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "机构ID")
    private Long institutionId;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "机构类型")
    private String institutionType;

    @Schema(description = "专家评委平均分（换算前）")
    private Double expertAvgScore;

    @Schema(description = "专家评委人数")
    private Long expertJudgeCount;

    @Schema(description = "专家分贡献（70%权重）")
    private Double expertContribution;

    @Schema(description = "大众评委平均分（换算前）")
    private Double publicAvgScore;

    @Schema(description = "大众评委人数")
    private Long publicJudgeCount;

    @Schema(description = "大众分贡献（20%权重）")
    private Double publicContribution;

    @Schema(description = "主任演讲加分（8分）")
    private Integer directorBonus;

    @Schema(description = "会务秘书加分（2分）")
    private Integer secretaryBonus;

    @Schema(description = "最终总分（满分100）")
    private Double finalScore;

    @Schema(description = "排名")
    private Integer rank;

    @Schema(description = "各条目得分明细")
    private List<ItemResultDTO> itemResults;

    @Data
    @Schema(description = "条目结果DTO")
    public static class ItemResultDTO {

        @Schema(description = "条目ID")
        private Long itemId;

        @Schema(description = "条目名称")
        private String itemName;

        @Schema(description = "所属分类名称")
        private String categoryName;

        @Schema(description = "条目满分")
        private Double maxScore;

        @Schema(description = "平均分")
        private Double avgScore;
    }
}
