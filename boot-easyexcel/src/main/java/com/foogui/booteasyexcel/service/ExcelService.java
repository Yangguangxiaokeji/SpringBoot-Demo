package com.foogui.booteasyexcel.service;

import com.foogui.booteasyexcel.vo.ExcelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExcelService {

   public int insertData(List<ExcelDTO> excelDTOS){
        // 模型批量插入DB成功
        log.info("Insert successfully");
        return excelDTOS.size();
    }
}
