package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "institution_bonus")
@Data
@Schema(description = "机构附加项实体（主任演讲、会务秘书）")
public class InstitutionBonus {

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
    @Schema(description = "所属机构")
    private Institution institution;

    @Column(nullable = false)
    @Schema(description = "是否有主任/副主任到场演讲：0-否，1-是（+8 分）")
    private Integer directorPresentation = 1;

    @Column(nullable = false)
    @Schema(description = "是否有人参与会务秘书：0-否，1-是（+2 分）")
    private Integer secretaryParticipation = 1;

    @Column(length = 50)
    @Schema(description = "填写人")
    private String filledBy;

    @Column
    @Schema(description = "填写时间")
    private LocalDateTime filledAt;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
