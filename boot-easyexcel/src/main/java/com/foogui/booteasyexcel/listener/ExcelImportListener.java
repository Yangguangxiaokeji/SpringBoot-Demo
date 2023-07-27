package com.foogui.booteasyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.foogui.booteasyexcel.service.ExcelService;
import com.foogui.booteasyexcel.vo.ExcelDTO;
import com.foogui.common.enums.ErrorCode;
import com.foogui.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * 监听器，每读一行数据就会触发一次监听相应动作
 *
 * @author Foogui
 * @date 2023/03/28
 */
@Slf4j
public class ExcelImportListener extends AnalysisEventListener<ExcelDTO> {

    private ExcelService excelService;
    private static final int BATCH_COUNT = 1000;
    List<ExcelDTO> insertList = Lists.newArrayList();

    public ExcelImportListener(ExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     * 每解析一行，会触发该方法往list添加元素
     *
     * @param excelDTO excel dto
     * @param context  上下文
     */
    @Override
    public void invoke(ExcelDTO excelDTO, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(excelDTO));

        insertList.add(excelDTO);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (insertList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            insertList.clear();
        }
    }

    private void saveData() {
        log.info("{}条数据，开始存储数据库！", insertList.size());
        // 插入数据库
        excelService.insertData(insertList);
    }


    /**
     *  出现异常回调
     *
     * @param exception 异常
     * @param context   上下文
     * @throws Exception 异常
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // ExcelDataConvertException:当数据转换异常的时候，会抛出该异常，此处可以得知第几行，第几列的数据
        if (exception instanceof ExcelDataConvertException) {
            int columnIndex = ((ExcelDataConvertException) exception).getColumnIndex() + 1;
            int rowIndex = ((ExcelDataConvertException) exception).getRowIndex() + 1;
            throw new BizException(ErrorCode.ERROR.getCode(), "第" + rowIndex + "行，第" + columnIndex + "列" + "数据格式有误，请核实");
        } else if (exception instanceof RuntimeException) {
            throw exception;
        } else {
            super.onException(exception, context);
        }
    }


    /**
     * 解析完整个文档后调用的方法
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 再进行一次插入数据库的操作，避免insertList数据中有遗留
        saveData();
        log.info("所有数据解析完成！");
    }
}
