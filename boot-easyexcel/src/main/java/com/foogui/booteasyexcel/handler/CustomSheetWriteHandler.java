package com.foogui.booteasyexcel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.foogui.booteasyexcel.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.HashMap;
import java.util.Map;

/**
 * 下拉框数据设置
 * 如果下拉框总的字符数大于255
 * @author Foogui
 * @date 2023/04/03
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {
    private int index=0;
    private String systemId;

    private ExcelService excelService;

    public CustomSheetWriteHandler(String systemId, ExcelService excelService) {
        this.systemId = systemId;
        this.excelService = excelService;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    /**
     * 从数据库中查询数据，并转化为数组设置到下拉框内
     *
     * @param writeWorkbookHolder 写工作簿持有人
     * @param writeSheetHolder    写单夹
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Map<Integer, String[]> mapDropDown = new HashMap<>();

        String[] dataArr = {"YES", "NO"};
        // 下拉框在Excel中对应的列,对应成员变量需要加index
        mapDropDown.put(0, dataArr);
        mapDropDown.put(1, dataArr);
        // 获取工作簿
        Sheet sheet = writeSheetHolder.getSheet();
        ///开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 设置下拉框，突破下拉框255个字符的限制
        mapDropDown.forEach((k, v) ->{
            //创建sheet，突破下拉框255的限制
            Workbook workbook =writeWorkbookHolder.getWorkbook();
            String sheetName = "sheet" +k;
            Sheet proviceSheet =workbook.createSheet(sheetName);
            this.index++;
            workbook.setSheetHidden(this.index, true);
            for (int i = 0, length = v.length; i < length; i++) {
                proviceSheet.createRow(i).createCell(0).setCellValue(v[i]);
            }
            Name category1Name =workbook.createName();
            category1Name.setNameName(sheetName);
            category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" +(v.length));
            CellRangeAddressList addressList = new CellRangeAddressList(1, 50, k, k);
            DataValidationConstraint constraint8 =helper.createFormulaListConstraint(sheetName);
            DataValidation dataValidation =helper.createValidation(constraint8, addressList);
            dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            dataValidation.setShowErrorBox(true);
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.createErrorBox("提示", "此值与单元格定义格式不一致");
            /*** 处理Excel兼容性问题 **/
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            writeSheetHolder.getSheet().addValidationData(dataValidation);

        });
    }
}