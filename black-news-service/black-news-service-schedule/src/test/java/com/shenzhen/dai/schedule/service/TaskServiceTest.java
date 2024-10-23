package com.shenzhen.dai.schedule.service;

import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.schedule.BlackNewsScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest(classes = BlackNewsScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void addTask() {
        Task task = new Task();
        task.setTaskType(100);
        task.setPriority(50);
        task.setParameters("task test".getBytes());
//        task.setExecuteTime(new Date().getTime());
//        task.setExecuteTime(new Date().getTime() + 500);
        task.setExecuteTime(new Date().getTime() + 500000);
        long taskId = taskService.addTask(task);
        System.out.println("taskId = " + taskId);
    }

    @Test
    public void cancelTask() {
        taskService.cancelTask(1848957616347496449L);
    }
}
