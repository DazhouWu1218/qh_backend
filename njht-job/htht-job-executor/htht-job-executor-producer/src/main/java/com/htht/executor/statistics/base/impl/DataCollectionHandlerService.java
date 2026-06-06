package com.htht.executor.statistics.base.impl;


import com.htht.executor.statistics.base.FileStatisticsJobService;
import com.htht.executor.statistics.constant.FileStatisticsTypeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhushizhen
 * @Date 2022-09-26 17:42
 **/
@SuppressWarnings("ALL")
@Service(FileStatisticsTypeConstant.SOURCE_DATA_COLLECTION)
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DataCollectionHandlerService extends FileStatisticsJobService {


}
