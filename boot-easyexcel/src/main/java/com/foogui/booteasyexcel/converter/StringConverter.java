package com.foogui.booteasyexcel.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class StringConverter implements Converter<String> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 将excel对象转成Java对象，这里读的时候会调用
     *
     * @param cellData            单元格数据
     * @param contentProperty     内容属性
     * @param globalConfiguration 全局配置
     * @return {@link String}
     * @throws Exception 异常
     */
    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return "自定义：" + cellData.getStringValue();
    }

    /**
     * 转将Java对象转成String对象，写出的时候调用
     *
     * @param value               值
     * @param contentProperty     内容属性
     * @param globalConfiguration 全局配置
     * @return {@link WriteCellData}<{@link ?}>
     * @throws Exception 异常
     */
    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return Converter.super.convertToExcelData(value, contentProperty, globalConfiguration);
    }
}
