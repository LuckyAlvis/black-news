package com.shenzhen.dai;

import com.shenzhen.dai.common.redis.CacheService;
import com.shenzhen.dai.schedule.BlackNewsScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;


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

    @Test
    public void main2() {
        cacheService.zAdd("zset_key001", "item1", 1000);
        cacheService.zAdd("zset_key001", "item2", 2000);
        cacheService.zAdd("zset_key001", "item3", 3000);
        cacheService.zAdd("zset_key001", "item4", 4000);

        Set<String> zsetKey001 = cacheService.zRangeByScore("zset_key001", 1000, 3000);
        System.out.println("zsetKey001 = " + zsetKey001);
    }
}
