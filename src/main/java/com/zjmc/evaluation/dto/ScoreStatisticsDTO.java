package com.zjmc.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(description = "评分统计DTO")
public class ScoreStatisticsDTO {

    @Schema(description = "总评分次数")
    private Long totalScoreCount;

    @Schema(description = "已评分专家数量")
    private Long expertJudgeCount;

    @Schema(description = "已评分大众评委数量")
    private Long publicJudgeCount;

    @Schema(description = "已完成评分机构数量")
    private Long completedInstitutionCount;

    @Schema(description = "机构得分排名Top10")
    private List<ScoreResultDTO> topRankings;

    @Schema(description = "各分类平均得分")
    private Map<String, Double> categoryAverages;

    @Schema(description = "评委评分进度统计")
    private Map<String, Long> judgeProgress;
}
