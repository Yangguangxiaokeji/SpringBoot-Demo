package com.foogui.common.domain;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class DTO extends PageRequest{

    private String condition;

}
