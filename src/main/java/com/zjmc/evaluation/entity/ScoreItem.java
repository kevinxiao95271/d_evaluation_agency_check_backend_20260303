package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_item")
@Data
@Schema(description = "评分条目实体")
public class ScoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "条目ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    @Schema(description = "所属模板")
    private ScoreTemplate template;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "所属分类")
    private ScoreCategory category;

    @Column(nullable = false, length = 200)
    @Schema(description = "条目名称/评分标准")
    private String name;

    @Column(length = 500)
    @Schema(description = "评分细则说明")
    private String description;

    @Column(nullable = false)
    @Schema(description = "条目满分值")
    private Double maxScore;

    @Column(nullable = false)
    @Schema(description = "权重（用于总分自动分配）")
    private Double weight;

    @Column(nullable = false)
    @Schema(description = "排序号")
    private Integer sortOrder;

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
