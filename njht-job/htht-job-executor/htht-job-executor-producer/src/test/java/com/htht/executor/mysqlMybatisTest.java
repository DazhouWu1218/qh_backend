package com.htht.executor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.htht.executor.product.entity.ProductInfoEntity;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.product.service.ProductService;
import com.htht.executor.task.util.PostgreSQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class mysqlMybatisTest {
    @Autowired
    private ProductInfoService productInfoService;


    @Test
    public void test001(){
        productInfoService.list();
    }

    @Test
    @Transactional(transactionManager = "mysqlTransactionManager",rollbackFor = Exception.class)
    public void test01() throws Exception {
        String cycle = "COAM";
        String productKey = "NPP";
        LambdaQueryWrapper<ProductInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<String> dbIssueList = productInfoService.list(queryWrapper.eq(StringUtils.isNotBlank(cycle),ProductInfoEntity::getCycle,cycle).eq(StringUtils.isNotBlank(productKey),
                ProductInfoEntity::getModelIdentify,productKey)).stream().map(productInfoEntity -> productInfoEntity.getIssue()).collect(Collectors.toList());
        System.out.println(dbIssueList.size());

        boolean flag = productInfoService.remove(queryWrapper.eq(StringUtils.isNotBlank(cycle),ProductInfoEntity::getCycle,cycle)
                .eq(StringUtils.isNotBlank(productKey),
                        ProductInfoEntity::getModelIdentify,productKey));
        System.out.println(flag);
        try {
            int j = 1/0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    @Test
    public void TestPgsql(){
        String geom = PostgreSQLUtils.selectCoordinate("NDVI");
        System.out.println(geom);
        boolean flag = PostgreSQLUtils.getTableName("NDVI_MM");
        System.out.println(flag);
    }

}
