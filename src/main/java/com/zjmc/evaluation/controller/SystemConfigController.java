package com.zjmc.evaluation.controller;

import com.zjmc.evaluation.common.Result;
import com.zjmc.evaluation.entity.SystemConfig;
import com.zjmc.evaluation.repository.SystemConfigRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/system-config")
@Tag(name = "系统配置", description = "系统配置管理")
public class SystemConfigController {

    @Autowired
    private SystemConfigRepository configRepository;

    @GetMapping("/list")
    @Operation(summary = "获取所有配置", description = "获取系统所有配置项")
    public Result<List<SystemConfig>> findAll() {
        return Result.success(configRepository.findAll());
    }

    @GetMapping("/{key}")
    @Operation(summary = "获取配置值", description = "根据配置键获取配置值")
    public Result<String> getConfigValue(
            @Parameter(description = "配置键") @PathVariable String key) {
        return configRepository.findByConfigKey(key)
            .map(config -> Result.success(config.getConfigValue()))
            .orElse(Result.success(""));
    }

    @PostMapping
    @Operation(summary = "创建配置", description = "创建新的系统配置")
    public Result<SystemConfig> create(@Valid @RequestBody SystemConfig config) {
        return Result.success(configRepository.save(config));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新配置", description = "更新系统配置")
    public Result<SystemConfig> update(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @Valid @RequestBody SystemConfig config) {
        config.setId(id);
        return Result.success(configRepository.save(config));
    }

    @PutMapping("/key/{key}")
    @Operation(summary = "更新配置值", description = "根据配置键更新配置值")
    public Result<Void> updateByKey(
            @Parameter(description = "配置键") @PathVariable String key,
            @Parameter(description = "配置值") @RequestParam String value) {
        SystemConfig config = configRepository.findByConfigKey(key)
            .orElse(new SystemConfig());
        config.setConfigKey(key);
        config.setConfigValue(value);
        if (config.getConfigName() == null) {
            config.setConfigName(key);
        }
        configRepository.save(config);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置", description = "删除系统配置")
    public Result<Void> delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        configRepository.deleteById(id);
        return Result.success();
    }

    @GetMapping("/total-score")
    @Operation(summary = "获取系统总分", description = "获取系统配置的考核总分")
    public Result<Double> getTotalScore() {
        return configRepository.findByConfigKey("total_score")
            .map(config -> Result.success(Double.parseDouble(config.getConfigValue())))
            .orElse(Result.success(45.0));
    }

    @PutMapping("/total-score")
    @Operation(summary = "设置系统总分", description = "设置系统考核总分")
    public Result<Void> setTotalScore(
            @Parameter(description = "总分值") @RequestParam Double score) {
        SystemConfig config = configRepository.findByConfigKey("total_score")
            .orElse(new SystemConfig());
        config.setConfigKey("total_score");
        config.setConfigName("系统总分");
        config.setConfigValue(String.valueOf(score));
        config.setDescription("考核评估系统的总分值");
        configRepository.save(config);
        return Result.success();
    }
}
