package com.shenzhen.dai.schedule.service;

import com.shenzhen.dai.common.redis.CacheService;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.schedule.BlackNewsScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
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
}
