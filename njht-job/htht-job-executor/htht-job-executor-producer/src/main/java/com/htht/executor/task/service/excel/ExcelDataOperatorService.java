package com.htht.executor.task.service.excel;

import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * excel 表格处理服务
 * @author zedong
 */
@Service
public class ExcelDataOperatorService<T> {

    @Autowired
    private ExcelListener<T> excelListener;

    /**
     * 异步执行方法，提交线程池执行
     * @param filePath
     * @param rowNum
     * @param tClass
     */
    @Async("taskExecutor")
    public void saveExcelDataToDbAsy(String filePath, int rowNum, Class tClass){
        EasyExcel.read(filePath,tClass,excelListener)
                .registerConverter(new CustomStringStringConverter())
                .sheet().headRowNumber(rowNum).doRead();
    }
}
