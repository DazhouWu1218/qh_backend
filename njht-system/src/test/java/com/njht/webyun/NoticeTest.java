package com.njht.webyun;

import com.njht.webyun.product.service.ProductCategoryService;
import com.njht.webyun.product.vo.ProductInfoTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/18 13:35
 * @Description: 12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NoticeTest {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    public void test() {

        List<ProductInfoTree> productInfoTreeList = productCategoryService.getProductInfoTreeList();
    }

}
