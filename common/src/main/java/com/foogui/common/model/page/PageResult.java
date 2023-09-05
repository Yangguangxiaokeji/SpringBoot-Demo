package com.foogui.common.model.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


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
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 6845513814060581181L;

    private List<T> data;

    private Long total;

    private Integer currentPage;

    private Integer pageSize;

}
