package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_template")
@Data
@Schema(description = "评分表模板实体")
public class ScoreTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "模板ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "模板名称")
    private String name;

    @Column(nullable = false)
    @Schema(description = "评分表满分值（如100分）")
    private Double templateMaxScore;

    @Column(nullable = false)
    @Schema(description = "系统总分值（如45分）")
    private Double systemTotalScore;

    @Column(nullable = false)
    @Schema(description = "是否默认模板：0-否，1-是")
    private Integer isDefault = 0;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status = 1;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
