package com.zjmc.evaluation.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "judge")
@Data
@Schema(description = "评委实体")
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "评委ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "评委姓名")
    private String name;

    @Column(nullable = false, length = 30)
    @Schema(description = "登录账号")
    private String username;

    @Column(nullable = false, length = 100)
    @Schema(description = "登录密码")
    private String password;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Schema(description = "评委类型：EXPERT-专家评委, PUBLIC-大众评委")
    private JudgeType type;

    @Column(length = 50)
    @Schema(description = "职称（可选），如：主任医师、副主任医师")
    private String title;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    @Schema(description = "所属机构（用于同机构回避）")
    private Institution institution;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

    @Column(length = 50)
    @Schema(description = "邮箱")
    private String email;

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

    public enum JudgeType {
        EXPERT,  // 专家评委
        PUBLIC   // 大众评委
    }
}
