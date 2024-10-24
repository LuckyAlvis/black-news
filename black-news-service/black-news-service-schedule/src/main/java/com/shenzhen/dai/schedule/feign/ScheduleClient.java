package com.shenzhen.dai.schedule.feign;

import com.shenzhen.dai.apis.schedule.IScheduleClient;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 延迟任务远程调用实现
 * @author: daiyifan
 * @create: 2024-10-24 10:27
 */
@RestController
public class ScheduleClient implements IScheduleClient {

    @Autowired
    private TaskService taskService;

    @Override
    @PostMapping("/api/v1/task/add")
    public ResponseResult<?> addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskService.addTask(task));
    }

    @Override
    @GetMapping("/api/v1/task/cancel/{taskId}")
    public ResponseResult<?> cancelTask(@PathVariable(value = "taskId") long taskId) {
        return ResponseResult.okResult(taskService.cancelTask(taskId));
    }

    @Override
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ResponseResult<?> pull(@PathVariable(value = "type") int type, @PathVariable(value = "priority") int priority) {
        return ResponseResult.okResult(taskService.pull(type, priority));
    }
}

