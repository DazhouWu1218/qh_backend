package com.htht.executor.task.service.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 表格内容读取与数据库保存（线程安全模式）
 * @param <T>
 * @author zedong
 */
@Slf4j
@Component("excelListener")
public class ExcelListener<T> extends AnalysisEventListener<T> {


    private static ThreadLocal<DateFormat> dateThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private static ThreadLocal<AtomicInteger> countThreadLocal = ThreadLocal.withInitial(() -> new AtomicInteger(1));

    private static ThreadLocal<AtomicInteger> titleThreadLocal = ThreadLocal.withInitial(() -> new AtomicInteger(1));

    private static ThreadLocal<Date> issueThreadLocal = ThreadLocal.withInitial(() -> new Date());

    private static ThreadLocal<List> dataListThreadLocal = ThreadLocal.withInitial(() -> new ArrayList());

    //一行一行读取excel内容
    @Override
    public void invoke( T t, AnalysisContext analysisContext) {
        countThreadLocal.get().incrementAndGet();
    }
    //读取表头内容
    @SneakyThrows
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (!headMap.isEmpty() && titleThreadLocal.get().get() == 1){
            String head = headMap.get(0);
        }
        titleThreadLocal.get().incrementAndGet();
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            saveStatisticsDataToDb();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error("保存失败");
        } finally {
            countThreadLocal.remove();
            titleThreadLocal.remove();
            issueThreadLocal.remove();
            dataListThreadLocal.remove();
        }
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }
    }

    /**
     * 保存到数据库中
     */
    private void saveStatisticsDataToDb() {
        List statisticDataList =  dataListThreadLocal.get();
        for (Object obj: statisticDataList){

        }

    }
}
