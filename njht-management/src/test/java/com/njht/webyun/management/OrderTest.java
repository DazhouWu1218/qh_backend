package com.njht.webyun.management;

import com.njht.webyun.management.common.util.ShpUtil;
import com.njht.webyun.management.order.service.OrderDownLoadService;
import com.njht.webyun.management.region.dao.RegionInfoDao;
import com.njht.webyun.utils.FileSearchUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2022/3/1 9:27
 * @Description: 订单测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderTest {

    @Autowired
    private OrderDownLoadService orderDownLoadService;

    @Test
    public void  test01(){
        orderDownLoadService.getOrderStatus();
    }

    @Autowired
    private RegionInfoDao regionInfoDao;

    @Test
    public void test02(){
        String filePath = "D:\\文档资料\\工作任务\\2023\\08通辽生态修复系统\\01SHP\\city";
        List<File> fileList = FileSearchUtils.getFileList(new File(filePath), ".*.shp$");

        for (File file : fileList) {
            Map<String, List<String>> map = ShpUtil.genFile(file.getPath());
            List<String> list = (List)map.get("values");

            for (String s : list) {
                String [] str1 = s.split(";");
                String geo = str1[0];
                String regionId = str1[5];
                System.out.println(regionId+"_"+str1[5]);
                regionInfoDao.insert(geo,regionId);
            }
        }


    }

}
