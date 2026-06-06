package com.htht.executor.task.handler.shard;


import com.htht.executor.product.service.ProductFileInfoService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.shard.SharingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wordToPdfHandlerShard")
public class WordToPdfHandlerShard implements SharingHandler {

    @Autowired
    private ProductFileInfoService productFileInfoService;

    @Override
    public ReturnT<List<String>> executeShard(TaskParam taskParam) {
        XxlJobHelper.log("开始执行.....word2pdf");
        // 以入库且没有转换的文件路径文件路径
        List<String> filePathList = productFileInfoService.getDocFileInfoList();
        XxlJobHelper.log("获取暂时没有转换的文件<<<<<<<总共"+filePathList.size()+"个文件");
        return ReturnT.success(filePathList);
    }
}
