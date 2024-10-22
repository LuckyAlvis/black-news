package com.shenzhen.dai;

import com.shenzhen.dai.common.redis.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = BlackNewsScheduleApplication.class)
@RunWith(SpringRunner.class)
public class BlackNewsScheduleApplicationTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void main() {
        cacheService.lLeftPush("list01", "item01");
    }

    @Test
    public void main1() {
        System.out.println("cacheService.lRightPop(\"list01\") = " + cacheService.lRightPop("list01"));
    }
}
