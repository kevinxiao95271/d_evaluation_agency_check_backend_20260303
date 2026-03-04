package com.zjmc.evaluation.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;

@Data
@Schema(description = "通用响应结果")
public class Result<T> {

    @Schema(description = "状态码，200表示成功")
    private Integer code;

    @Schema(description = "提示信息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> emptyList() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData((T) Collections.emptyList());
        return result;
    }
}
