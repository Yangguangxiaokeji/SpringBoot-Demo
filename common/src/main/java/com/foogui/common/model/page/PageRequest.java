package com.foogui.common.model.page;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页参数
 *
 * @author wangxin
 * @date 2023/09/04
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -2432837727476621461L;
    /**
     * 每页数目
     */
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
    /**
     * 页码
     */
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;

}
