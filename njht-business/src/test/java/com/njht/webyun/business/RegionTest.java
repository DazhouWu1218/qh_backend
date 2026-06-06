package com.njht.webyun.business;

import com.njht.webyun.business.datapush.handler.DataPushHandler;
import com.njht.webyun.business.index.service.RegistryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/27 9:21
 * @Description: 1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RegionTest {

    @Autowired
    private DataPushHandler dataPushHandler;

    @Autowired
    private RegistryService registryService;

    @Test
    public void test() {
        dataPushHandler.dataPushHandler();
//        registryService.serverList();
    }

//    public static void main(String[] args) {
//        String path = "F:\\EAMIS\\123\\1.txt";
//        File file = new File(path);
//        System.out.println(file.length());
//    }
}
