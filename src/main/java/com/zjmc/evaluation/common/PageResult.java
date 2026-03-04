package com.zjmc.evaluation.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分页响应结果")
public class PageResult<T> {

    @Schema(description = "当前页码")
    private Integer pageNum;

    @Schema(description = "每页大小")
    private Integer pageSize;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Integer pages;

    @Schema(description = "数据列表")
    private List<T> list;

    public static <T> PageResult<T> of(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / pageSize));
        result.setList(list);
        return result;
    }
}
