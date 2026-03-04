package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_institution")
@Data
@Schema(description = "任务-机构关联实体（受评任务）")
public class TaskInstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @Schema(description = "所属任务")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    @Schema(description = "受评机构")
    private Institution institution;

    @Column(length = 500)
    @Schema(description = "材料说明")
    private String materialDescription;

    @Column(nullable = false)
    @Schema(description = "状态：0-未开始，1-评审中，2-已完成")
    private Integer status = 0;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
