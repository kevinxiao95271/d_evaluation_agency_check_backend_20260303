package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_record")
@Data
@Schema(description = "评分记录实体")
public class ScoreRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @Schema(description = "所属任务")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    @Schema(description = "受评机构")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "judge_id", nullable = false)
    @Schema(description = "评分评委")
    private Judge judge;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @Schema(description = "评分条目")
    private ScoreItem item;

    @Column(nullable = false)
    @Schema(description = "评分分数")
    private Double score;

    @Column(length = 10)
    @Schema(description = "评分模式：ITEM-逐条打分，TOTAL-直接打总分")
    private String scoreMode;

    @Column(length = 500)
    @Schema(description = "评语/备注")
    private String comment;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
