package com.foogui.common.model.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;


/**
 * 分页结果
 *
 * @author wangxin
 * @date 2023/09/04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<R> implements Serializable {

    private static final long serialVersionUID = 6845513814060581181L;

    private List<R> data;

    private Long total;

    private Integer currentPage;

    private Integer pageSize;

    public <R> PageResult<R> getPageResult(int pageNum, int pageSize, Supplier<List<? extends R>> supplier) {

        PageHelper.startPage(pageNum, pageSize);

        PageInfo<R> pageInfo = new PageInfo<>(supplier.get());

        return PageResult.<R>builder()
                .data(pageInfo.getList())
                .total(pageInfo.getTotal())
                .currentPage(pageNum)
                .pageSize(pageSize)
                .build();
    }

}
