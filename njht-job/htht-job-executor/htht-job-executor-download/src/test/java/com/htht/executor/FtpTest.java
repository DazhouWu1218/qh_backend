package com.htht.executor;


import com.htht.executor.download.entity.FtpEntity;
import com.htht.executor.task.service.BaseDownJobService;
import com.htht.executor.task.strategy.DownJobShardContext;
import com.htht.executor.task.util.ApacheFtpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FtpTest {

    @Autowired
    private DownJobShardContext downJobShardContext;

    @Test
    public void test01() {
        BaseDownJobService baseDownJobService = downJobShardContext.getDownJobShardByType("CHENQING");
    }


    public static void main(String[] args) throws IOException {
        FtpEntity ftpDTO = new FtpEntity();
        String username = "HTNanjing";
        String pwd = "HNg@!21";
        Integer port = 21;
        String ip = "221.122.67.132";

        String  filePath = "/H8_2019/2019/201904/20190401/";

        ftpDTO.setIpAddr(ip);
        ftpDTO.setPort(port);
        ftpDTO.setUserName(username);
        ftpDTO.setPwd(pwd);
        ApacheFtpUtil ftpUtil = new ApacheFtpUtil(ftpDTO);
        List<String> pathList = new ArrayList<>();
        if(ftpUtil.connectServer()) {
            ftpUtil.getDataFileAndDirectoryList(filePath,".*.",pathList);
            pathList.forEach(System.out::println);

            ftpUtil.closeServer();
        }
    }
}
