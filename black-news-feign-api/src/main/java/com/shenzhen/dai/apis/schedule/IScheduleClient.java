package com.shenzhen.dai.apis.schedule;

import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.schedule.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "black-news-schedule")
public interface IScheduleClient {
    /**
     * 添加延时任务
     *
     * @param task
     * @return
     */
    @PostMapping("/api/v1/task/add")
    ResponseResult addTask(@RequestBody Task task);

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    ResponseResult cancelTask(@PathVariable(value = "taskId") long taskId);

    /**
     * 拉取任务
     *
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    ResponseResult pull(@PathVariable(value = "type") int type, @PathVariable(value = "priority") int priority);
}
