package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "institution")
@Data
@Schema(description = "机构实体")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "机构ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "机构名称")
    private String name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Schema(description = "机构类型：TECHNICAL_SERVICE-技术服务类, QUALITY_CONTROL-专业质控中心")
    private InstitutionType type;

    @Column(length = 200)
    @Schema(description = "机构描述")
    private String description;

    @Column(length = 50)
    @Schema(description = "负责人")
    private String director;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

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

    public enum InstitutionType {
        TECHNICAL_SERVICE,  // 技术服务类
        QUALITY_CONTROL     // 专业质控中心
    }
}
