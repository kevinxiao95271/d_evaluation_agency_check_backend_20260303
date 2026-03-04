package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_category")
@Data
@Schema(description = "评分分类实体（一级分类）")
public class ScoreCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分类ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    @Schema(description = "所属模板")
    private ScoreTemplate template;

    @Column(nullable = false, length = 50)
    @Schema(description = "分类名称")
    private String name;

    @Column(nullable = false)
    @Schema(description = "排序号")
    private Integer sortOrder;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
