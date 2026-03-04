package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.dto.ScoreRecordDTO;
import com.zjmc.evaluation.dto.ScoreResultDTO;
import com.zjmc.evaluation.dto.ScoreStatisticsDTO;
import com.zjmc.evaluation.dto.ScoreSubmissionDTO;
import com.zjmc.evaluation.dto.ScoreSubmitDTO;
import com.zjmc.evaluation.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/score")
@Tag(name = "评分管理", description = "评分录入和计算相关接口")
public class ScoreController {

    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private ScoreService scoreService;

    @PostMapping("/submit")
    @Operation(summary = "提交评分", description = "评委提交评分（支持逐条打分和直接打总分两种模式）")
    public Result<Void> submitScore(@Valid @RequestBody ScoreSubmitDTO dto) {
        logger.info("Received score submission: {}", dto);
        scoreService.submitScore(dto);
        return Result.success();
    }

    @GetMapping("/records")
    @Operation(summary = "获取评分记录列表", description = "支持按评委、机构、任务筛选评分记录(条目明细级别)")
    public Result<List<ScoreRecordDTO>> findScoreRecords(
            @Parameter(description = "任务ID") @RequestParam(required = false) Long taskId,
            @Parameter(description = "机构ID") @RequestParam(required = false) Long institutionId,
            @Parameter(description = "评委ID") @RequestParam(required = false) Long judgeId) {
        return Result.success(scoreService.findScoreRecords(taskId, institutionId, judgeId));
    }
    
    @GetMapping("/submissions")
    @Operation(summary = "获取评分提交记录列表", description = "支持按评委、机构、任务筛选评分提交记录(汇总级别,包含明细)")
    public Result<List<ScoreSubmissionDTO>> findScoreSubmissions(
            @Parameter(description = "任务ID") @RequestParam(required = false) Long taskId,
            @Parameter(description = "机构ID") @RequestParam(required = false) Long institutionId,
            @Parameter(description = "评委ID") @RequestParam(required = false) Long judgeId) {
        return Result.success(scoreService.findScoreSubmissions(taskId, institutionId, judgeId));
    }

    @GetMapping("/record/{id}")
    @Operation(summary = "获取评分详情", description = "根据ID获取单条评分记录详情")
    public Result<ScoreRecordDTO> findRecordById(@Parameter(description = "记录ID") @PathVariable Long id) {
        return Result.success(scoreService.findRecordById(id));
    }

    @DeleteMapping("/record/{id}")
    @Operation(summary = "删除评分记录", description = "删除指定的评分记录")
    public Result<Void> deleteRecord(@Parameter(description = "记录ID") @PathVariable Long id) {
        scoreService.deleteRecord(id);
        return Result.success();
    }

    @PostMapping("/records/batch-delete")
    @Operation(summary = "批量删除评分记录", description = "根据ID列表批量删除评分记录")
    public Result<Void> batchDeleteRecords(@RequestBody List<Long> ids) {
        scoreService.batchDeleteRecords(ids);
        return Result.success();
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取任务评分统计", description = "获取指定任务的总体评分统计信息")
    public Result<ScoreStatisticsDTO> getScoreStatistics(
            @Parameter(description = "任务ID") @RequestParam Long taskId) {
        return Result.success(scoreService.getScoreStatistics(taskId));
    }

    @GetMapping("/judge-stats")
    @Operation(summary = "获取评委评分统计", description = "获取指定评委的评分进度和统计")
    public Result<ScoreStatisticsDTO> getJudgeStatistics(
            @Parameter(description = "评委ID") @RequestParam Long judgeId) {
        return Result.success(scoreService.getJudgeStatistics(judgeId));
    }

    @GetMapping("/institution-stats")
    @Operation(summary = "获取机构评分统计", description = "获取指定机构的得分统计")
    public Result<ScoreStatisticsDTO> getInstitutionStatistics(
            @Parameter(description = "机构ID") @RequestParam Long institutionId) {
        return Result.success(scoreService.getInstitutionStatistics(institutionId));
    }

    @GetMapping({"/export", "/export/excel"})
    @Operation(summary = "导出评分记录", description = "将评分记录导出为CSV文件")
    public void exportScores(
            @Parameter(description = "任务ID") @RequestParam Long taskId,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"scores.csv\"");
        
        List<ScoreRecordDTO> records = scoreService.findScoreRecords(taskId, null, null);
        
        try (PrintWriter writer = response.getWriter()) {
            // 写入BOM头，防止Excel打开中文乱码
            writer.write('\ufeff');
            writer.println("记录ID,任务名称,机构名称,评委姓名,评委类型,评分条目,分类,分数,满分,备注,评分时间");
            for (ScoreRecordDTO r : records) {
                writer.println(String.format("%d,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%s,%s",
                        r.getId(), r.getTaskName(), r.getInstitutionName(), r.getJudgeName(),
                        r.getJudgeType(), r.getItemName(), r.getCategoryName(), r.getScore(),
                        r.getMaxScore(), r.getComment() != null ? r.getComment() : "",
                        r.getCreateTime()));
            }
        }
    }

    @GetMapping("/result")
    @Operation(summary = "计算单个机构得分", description = "计算指定任务和机构的最终得分")
    public Result<ScoreResultDTO> calculateResult(
            @Parameter(description = "任务ID") @RequestParam Long taskId,
            @Parameter(description = "机构ID") @RequestParam Long institutionId) {
        return Result.success(scoreService.calculateResult(taskId, institutionId));
    }

    @GetMapping("/results/{taskId}")
    @Operation(summary = "计算所有机构得分", description = "计算指定任务下所有机构的最终得分和排名")
    public Result<List<ScoreResultDTO>> calculateAllResults(
            @Parameter(description = "任务ID") @PathVariable Long taskId) {
        List<ScoreResultDTO> list = scoreService.getResultsWithData(taskId);
        return Result.success(list);
    }
}
