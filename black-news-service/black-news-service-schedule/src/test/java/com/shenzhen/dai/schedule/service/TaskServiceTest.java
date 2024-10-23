package com.shenzhen.dai.schedule.service;

import com.alibaba.fastjson.JSON;
import com.shenzhen.dai.common.redis.CacheService;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.schedule.BlackNewsScheduleApplication;
import jakarta.annotation.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = BlackNewsScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void addTask() {
        Task task = new Task();
        task.setTaskType(100);
        task.setPriority(50);
        task.setParameters("task test".getBytes());
//        task.setExecuteTime(new Date().getTime());
//        task.setExecuteTime(new Date().getTime() + 500);
        task.setExecuteTime(new Date().getTime());
        long taskId = taskService.addTask(task);
        System.out.println("taskId = " + taskId);
    }

    @Test
    public void cancelTask() {
        taskService.cancelTask(1848957616347496449L);
    }

    @Test
    public void pull() {
        Task task = taskService.pull(100, 50);
        System.out.println("task = " + task);
    }

    @Test
    public void keys() {
        String pattern = "future_*";
        Set<String> keys = cacheService.keys(pattern);
        keys.forEach(System.out::println);
        Set<String> scan = cacheService.scan(pattern);
        scan.forEach(System.out::println);
    }

    //耗时6151
    @Test
    public void testPiple1() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Task task = new Task();
            task.setTaskType(1001);
            task.setPriority(1);
            task.setExecuteTime(new Date().getTime());
            cacheService.lLeftPush("1001_1", JSON.toJSONString(task));
        }
        System.out.println("不使用管道技术执行10000次自增操作共耗时" + (System.currentTimeMillis() - start));

    }


    @Test
    public void testPiple2() {
//        不使用管道技术执行10000次自增操作共耗时3061
//        使用管道技术执行10000次自增操作共耗时:697毫秒

//        不使用管道技术执行1000000次自增操作共耗时:208647毫秒
//        使用管道技术执行1000000次自增操作共耗时:11746毫秒

        long start = System.currentTimeMillis();
        //使用管道技术
        List<Object> objectList = cacheService.getstringRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < 1000000; i++) {
                    Task task = new Task();
                    task.setTaskType(1001);
                    task.setPriority(1);
                    task.setExecuteTime(new Date().getTime());
                    redisConnection.lPush("1001_1".getBytes(), JSON.toJSONString(task).getBytes());
                }
                return null;
            }
        });
        System.out.println("使用管道技术执行10000次自增操作共耗时:" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
