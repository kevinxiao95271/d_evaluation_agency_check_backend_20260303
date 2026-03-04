package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@Tag(name = "系统管理", description = "系统配置相关接口")
public class SystemController {

    @Value("${evaluation.total-score:45}")
    private Double totalScore;

    @Value("${evaluation.expert-weight:0.7}")
    private Double expertWeight;

    @Value("${evaluation.public-weight:0.2}")
    private Double publicWeight;

    @Value("${evaluation.director-bonus:8}")
    private Integer directorBonus;

    @Value("${evaluation.secretary-bonus:2}")
    private Integer secretaryBonus;

    @GetMapping("/config")
    @Operation(summary = "获取系统配置", description = "获取系统评分配置参数")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("totalScore", totalScore);
        config.put("expertWeight", expertWeight);
        config.put("publicWeight", publicWeight);
        config.put("directorBonus", directorBonus);
        config.put("secretaryBonus", secretaryBonus);
        config.put("expertWeightPercent", (int)(expertWeight * 100) + "%");
        config.put("publicWeightPercent", (int)(publicWeight * 100) + "%");
        return Result.success(config);
    }

    @GetMapping("/score-rules")
    @Operation(summary = "获取计分规则", description = "获取评分计算规则说明")
    public Result<Map<String, Object>> getScoreRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("formula", "总分 = 专家平均分 × 70% + 大众平均分 × 20% + 主任演讲(8分) + 会务秘书(2分)");
        rules.put("expertWeight", "70%");
        rules.put("publicWeight", "20%");
        rules.put("directorBonus", "8分（考核委员会勾选）");
        rules.put("secretaryBonus", "2分（考核委员会勾选）");
        rules.put("trimRule", "去掉一个最高分和一个最低分后取平均");
        rules.put("conversionRule", "评分表满分与系统总分自动换算");
        return Result.success(rules);
    }
}
