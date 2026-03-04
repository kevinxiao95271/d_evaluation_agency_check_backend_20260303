package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
@Schema(description = "考核任务实体")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "任务ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "任务名称，如：2026Q1考核")
    private String name;

    @Column(nullable = false, length = 20)
    @Schema(description = "考核周期，如：2026Q1")
    private String period;

    @Column(nullable = false)
    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Column(nullable = false)
    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Column(length = 500)
    @Schema(description = "任务描述/材料说明")
    private String description;

    @Column(nullable = false)
    @Schema(description = "是否为当前任务：0-否，1-是")
    private Integer isCurrent = 0;

    @Column(nullable = false)
    @Schema(description = "状态：0-未开始，1-进行中，2-已结束")
    private Integer status = 0;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
