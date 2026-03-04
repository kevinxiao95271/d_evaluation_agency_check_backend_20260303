package com.zjmc.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "评委DTO")
public class JudgeDTO {

    @Schema(description = "评委ID")
    private Long id;

    @NotBlank(message = "评委姓名不能为空")
    @Schema(description = "评委姓名", required = true)
    private String name;

    @NotBlank(message = "登录账号不能为空")
    @Schema(description = "登录账号", required = true)
    private String username;

    @Schema(description = "登录密码（新增时必填）")
    private String password;

    @NotNull(message = "评委类型不能为空")
    @Schema(description = "评委类型：EXPERT-专家评委, PUBLIC-大众评委", required = true)
    private String type;

    @Schema(description = "职称（可选），如：主任医师、副主任医师")
    private String title;

    @Schema(description = "所属机构ID（用于同机构回避）")
    private Long institutionId;

    @Schema(description = "所属机构名称")
    private String institutionName;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;
}
