package com.shenzhen.dai.schedule.service;

import com.shenzhen.dai.model.schedule.Task;

public interface TaskService {
    /**
     * 添加延时任务
     *
     * @param task
     * @return
     */
    long addTask(Task task);


}
