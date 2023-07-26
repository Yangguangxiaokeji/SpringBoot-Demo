package com.foogui.foo.generator.service;

import com.foogui.foo.generator.domain.GenDTO;
import com.foogui.foo.generator.domain.Table;
import net.sf.jsqlparser.JSQLParserException;

public interface GenService {


    /**
     * 通过json生成代码
     *
     * @param table 表信息
     * @return 数据
     */
    byte[] doCreateCodeByJSON(Table table);

    /**
     * 通过ddl创建代码
     *
     * @param ddl ddl
     * @param dto dto
     * @return {@link byte[]}
     * @throws JSQLParserException jsqlparser异常
     */
    byte[] doCreateCodeByDDL(GenDTO dto);


    /**
     * 批量生成代码
     *
     * @param dto dto
     * @return {@link byte[]}
     */
    public byte[] doCreateCodeBatch(GenDTO dto);

}
