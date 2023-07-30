package com.foogui.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto
 *
 * @author Foogui
 * @date 2023/07/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataDTO extends PageRequest{

    private String condition;

}
