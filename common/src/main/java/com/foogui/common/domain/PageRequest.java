package com.foogui.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页请求参数
 *
 * @author Foogui
 * @date 2023/07/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -2432837727476621461L;
    /**
     * 每页数目
     */
    private Integer pageSize = 20;
    /**
     * 页码
     */
    private Integer pageNum = 1;

}
