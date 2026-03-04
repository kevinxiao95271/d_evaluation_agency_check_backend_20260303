package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_config")
@Data
@Schema(description = "系统配置实体")
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "配置ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "配置项编码")
    private String configKey;

    @Column(nullable = false, length = 100)
    @Schema(description = "配置项名称")
    private String configName;

    @Column(nullable = false, length = 100)
    @Schema(description = "配置值")
    private String configValue;

    @Column(length = 200)
    @Schema(description = "配置说明")
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
