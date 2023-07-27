package com.foogui.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 分页结果
 *
 * @author Foogui
 * @date 2023/07/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 2315015017133819379L;

    private List<T> data;

    private Long total;

    private Integer  currentPage;

    private Integer  pageSize;

    private Boolean hasNextPage;

}
