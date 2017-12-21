package com.yykj.mall.service.impl;

import com.yykj.mall.service.IOrderService;
import com.yykj.mall.service.IShippingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Lee
 * @date 2017/12/14
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:applicationContext.xml")
public class OrderServiceImplTest {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShippingService shippingService;

    @Test
    public void getCartCheckedProduct() throws Exception {
        System.out.println(shippingService.list(26, 1, 10));
    }

}