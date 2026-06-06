package com.htht.executor.statistics.base.impl;


import com.htht.executor.product.service.ProductFileInfoService;
import com.htht.executor.statistics.base.FileStatisticsJobService;
import com.htht.executor.statistics.constant.FileStatisticsTypeConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import com.njht.entity.dataReport.DataReportEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/28 16:49
 */
@Service(FileStatisticsTypeConstant.SOURCE_PRODUCT)
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductHandlerService extends FileStatisticsJobService {
    @Autowired
    private ProductFileInfoService productFileInfoService;

    /**
     * 查找product_file_info表，形成report统计信息
     *
     * @param dataBaseInfoEntity
     * @param issueList
     * @param cycle
     * @return
     */
    @Override
    protected List<DataReportEntity> updateReportInfo(DataBaseInfoEntity dataBaseInfoEntity, List<String> issueList, String cycle) {
        List<DataReportEntity> updateReportList = new ArrayList<>();

        XxlJobHelper.log("进行文件扫描：");
        for(String issue : issueList) {
            XxlJobHelper.log("开始查找{}期次数据", issue);
            //查找表中该周期产品的文件个数
            List<Long> scanFileList = productFileInfoService.selectFileSize(dataBaseInfoEntity.getId(),issue,cycle);
            if (scanFileList.isEmpty()) {
                XxlJobHelper.log("未找到{}期次文件数据，数据无需统计！", issue);
                continue;
            } else {
                XxlJobHelper.log("找到期次为{}文件，个数为：{},", issue, scanFileList.size());
            }
            Long fileNum = (long) scanFileList.size();
            String status = "1";
            Long fileSize = 0L;
            fileSize = scanFileList.stream().reduce(0L, Long::sum);

            DataReportEntity dataReportEntity = new DataReportEntity();
            dataReportEntity.setIssue(issue);
            dataReportEntity.setFileNum(fileNum);
            dataReportEntity.setSumNum(fileNum);
            dataReportEntity.setFileSize(fileSize);
            dataReportEntity.setStatus(status);
            dataReportEntity.setDataId(dataBaseInfoEntity.getId());

            updateReportList.add(dataReportEntity);
        }
        return updateReportList;
    }
}
