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

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    boolean cancelTask(long taskId);

    /**
     * 拉取任务
     *
     * @param type
     * @param priority
     * @return
     */
    Task pull(int type, int priority);
}
