package com.htht.executor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.htht.executor.product.entity.ProductInfoEntity;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.transaction.DataSourceTransactionals;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class testController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    @Qualifier("pgsqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    @RequestMapping("/transaction")
//    @Transactional(transactionManager = "mysqlTransactionManager",rollbackFor = Exception.class)
    @DataSourceTransactionals(transactionManagers = {"pgsqlTransactionManager"})
    public void testTransaction() throws Exception {
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

        String sql = "INSERT INTO \"public\".\"NDVI\" VALUES ('3', '0103000020E610000001000000050000004B79AED1B74B5E40CE42E50FF9B545404B79AED1B74B5E40F5080495E4C74A40C3558530BDD86040F5080495E4C74A40C3558530BDD86040CE42E50FF9B545404B79AED1B74B5E40CE42E50FF9B54540', null, null, null)";
        jdbcTemplate.execute(sql);

        try {
            int j = 1/0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @RequestMapping("/pgsql")
//    @Transactional(transactionManager = "pgsqlTransactionManager")
    @DataSourceTransactionals(transactionManagers = {"pgsqlTransactionManager"})
    public void testPgsqlJdbc(){
        String sql = "INSERT INTO \"public\".\"NDVI\" VALUES ('3', '0103000020E610000001000000050000004B79AED1B74B5E40CE42E50FF9B545404B79AED1B74B5E40F5080495E4C74A40C3558530BDD86040F5080495E4C74A40C3558530BDD86040CE42E50FF9B545404B79AED1B74B5E40CE42E50FF9B54540', null, null, null)";
        jdbcTemplate.execute(sql);
        int j = 1/0;
    }


}
