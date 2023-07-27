package com.foogui.booteasyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.foogui.booteasyexcel.converter.DateConverter;
import com.foogui.booteasyexcel.handler.CustomSheetWriteHandler;
import com.foogui.booteasyexcel.listener.ExcelImportListener;
import com.foogui.booteasyexcel.service.ExcelService;
import com.foogui.booteasyexcel.vo.ExcelDTO;
import com.foogui.common.enums.ErrorCode;
import com.foogui.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String UTF_8 = "UTF-8";

    @Autowired
    private ExcelService excelService;

    /**
     * 下载模板,动态生成,含下拉框
     *
     * @param response 响应
     * @throws IOException ioexception
     */

    @GetMapping(value = "/downloadTemplate/{systemId}")
    public void downloadTemplate(HttpServletResponse response, @PathVariable("systemId") String systemId)  {
        try {
            prepareResponse(response,"物料信息");
            // 这里需要设置不关闭流
            EasyExcelFactory.write(response.getOutputStream(), ExcelDTO.class)
                    .autoCloseStream(Boolean.FALSE)
                    // 根据查询的数据动态设置下拉框
                    .registerWriteHandler(new CustomSheetWriteHandler(systemId, excelService))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("模板Sheet")
                    .doWrite(Collections.emptyList());
        } catch (IOException e) {
            log.error("exportExcelFile.failed,", e);
            throw new BizException(ErrorCode.ERROR.getCode(), "导出异常");
        }
    }

    /**
     * 下载模板,根据已存在的excel
     *
     * @param response 响应
     * @throws IOException ioexception
     */
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) throws IOException {

        prepareResponse(response,"物料信息");
        //获取文件的路径
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template/model.xlsx");
            //读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("下载模板出错,", e);
            throw new BizException(ErrorCode.ERROR.getCode(), "下载模板出错");
        }
    }

    /**
     * 导入excel
     *
     * @param file 文件
     */
    @PostMapping("/import")
    public void importExcel(@RequestParam("file") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            List<ExcelDTO> excelDTOS = EasyExcelFactory.read(inputStream)
                    // 这个转换是成全局的， 所有java为string,excel为string的都会用这个转换器。
                    .registerConverter(new DateConverter())
                    // 注册监听器，可以在这里校验字段,这里注册监听器必须要new，或者让Spring接管Listener但是需要加@Scope("prototype")
                    .registerReadListener(new ExcelImportListener(excelService))
                    // 设置列头
                    .head(ExcelDTO.class)
                    // 设置标题所在行数
                    .headRowNumber(1)
                    // 设置sheet,默认读取第一个
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            log.error("exportExcelFile.failed,", e);
            throw new BizException(ErrorCode.ERROR.getCode(), "导入异常");
        }
    }

    /**
     * 导出excel
     *
     * @param response 响应
     * @throws IOException ioexception
     */
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 模拟从数据库查询，生成数据
        List<ExcelDTO> data = Lists.newArrayList();
        for (int i = 0; i < 50; i++) {
            ExcelDTO excelDTO = ExcelDTO.builder()
                    .materialCode(String.valueOf(i))
                    .materialName("customerName" + i)
                    .materialMoney(new BigDecimal(String.valueOf(i)))
                    .startDate(LocalDateTime.now())
                    .endDate(new Date())
                    .build();
            data.add(excelDTO);
        }
        // 设置相应的相关格式
        prepareResponse(response,"物料信息");

        EasyExcel.write(response.getOutputStream(), ExcelDTO.class)
                .sheet("sheet0")
                // 设置字段宽度为自动调整，不太精确
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }




    /**
     * 设置响应格式
     *
     * @param response 响应
     * @param fileName 文件名称
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void prepareResponse(HttpServletResponse response,String fileName) throws UnsupportedEncodingException {
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(UTF_8);
        // 防止中文乱码
        String encodedName = URLEncoder.encode(fileName, UTF_8);
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedName + ".xlsx");
    }
}


