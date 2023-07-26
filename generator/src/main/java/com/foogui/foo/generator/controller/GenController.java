package com.foogui.foo.generator.controller;

import com.foogui.foo.generator.domain.GenDTO;
import com.foogui.foo.generator.domain.Table;
import com.foogui.foo.generator.service.GenService;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequestMapping("/gen")
@RestController
public class GenController {
    @Autowired
    private GenService genGenService;

    /**
     * 生成代码（下载方式）,json传递的格式参考项目中的demo.json
     */
    @PostMapping("/code")
    public void genCodeThroughJSON(HttpServletResponse response, @RequestBody Table table) throws IOException {
        byte[] data = genGenService.doCreateCodeByJSON(table);
        doCreateZip(response, data, table.getProjectName());
    }


    /**
     * 通过ddl的sql生成代码
     *
     * @param response 响应
     * @param file     文件
     * @throws IOException         ioexception
     * @throws JSQLParserException sql解析异常
     */
    @PostMapping("/sql")
    public void genCodeThroughDDLFile(HttpServletResponse response, @RequestParam("file") MultipartFile file, GenDTO dto) throws IOException {
        String ddl = new String(file.getBytes(), StandardCharsets.UTF_8);
        dto.setDdl(ddl);
        byte[] data = genGenService.doCreateCodeByDDL(dto);
        doCreateZip(response, data, dto.getProjectName());
    }


    /**
     * 通过表名批量生成代码
     *
     * @param response 响应
     * @param dto      dto
     * @throws IOException         ioexception
     * @throws JSQLParserException jsqlparser异常
     */
    @PostMapping("/batch")
    public void genCodeByTableNames(HttpServletResponse response, @RequestBody GenDTO dto) throws IOException {
        byte[] data = genGenService.doCreateCodeBatch(dto);
        doCreateZip(response, data, dto.getProjectName());
    }

    private void doCreateZip(HttpServletResponse response, byte[] data, String zipName) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName + ".zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
