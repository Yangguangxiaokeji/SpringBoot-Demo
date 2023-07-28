package com.foogui.booteasyexcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.foogui.booteasyexcel.valid.ExcelDigits;
import com.foogui.booteasyexcel.valid.ExcelNotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(10)
@HeadRowHeight(20)
@ColumnWidth(25)
public class ExcelDTO {
    /**
     * 物料名称
     */
    @ColumnWidth(50)                // 宽度为50
    @ExcelProperty(index = 0,value = "物料名称")
    @ExcelNotNull(message = "物料名不能为空")
    private String materialName;
    /**
     * 物料编码
     */
    @ExcelProperty(index = 1,value = "物料编码")
    @ExcelDigits(message = "物料编码必须是数字")
    private String materialCode;
    /**
     * 物料金额
     */
    @NumberFormat("#.##")
    @ExcelProperty(index = 2,value = "物料金额")
    private BigDecimal materialMoney;

    /**
     * 开始日期(LocalDateTime不需要转格式)
     */
    // @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 3,value = "开始日期")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    // @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 4,value = "结束日期")
    private Date endDate;
}
