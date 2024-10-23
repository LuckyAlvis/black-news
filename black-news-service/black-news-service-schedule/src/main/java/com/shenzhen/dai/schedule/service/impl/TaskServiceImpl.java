package com.shenzhen.dai.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.shenzhen.dai.common.constant.ScheduleConstants;
import com.shenzhen.dai.common.redis.CacheService;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.model.schedule.Taskinfo;
import com.shenzhen.dai.model.schedule.TaskinfoLogs;
import com.shenzhen.dai.schedule.mapper.TaskinfoLogsMapper;
import com.shenzhen.dai.schedule.mapper.TaskinfoMapper;
import com.shenzhen.dai.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-22 23:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public long addTask(Task task) {
        // 1.添加任务到数据库
        boolean success = addTaskToDb(task);

        // 2.添加任务到redis
        if (success) {
            addTaskToCache(task);
        }

        // 2.1 如果任务的执行时间小于当前时间，则存入list中

        // 2.2 如果任务的执行时间大于当前时间，
        return task.getTaskId();
    }

    @Autowired
    private CacheService cacheService;

    /**
     * 把任务添加到redis中
     *
     * @param task
     */
    private void addTaskToCache(Task task) {

        String key = task.getTaskType() + "_" + task.getPriority();

        // 获取5分钟之后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        //2.1 如果任务的执行时间小于等于当前时间，存入list
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            //2.2 如果任务的执行时间大于当前时间 && 小于等于预设时间（未来5分钟）存入zset中
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }

    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    /**
     * 添加任务到数据库中
     *
     * @param task
     * @return
     */
    private boolean addTaskToDb(Task task) {
        boolean flag = false;
        try {
            //保存任务表
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);

            // 设置taskId
            task.setTaskId(taskinfo.getTaskId());

            // 保存任务日志数据
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean cancelTask(long taskId) {
        boolean flag = false;
        // 删除任务，更新任务日志
        Task task = updateDb(taskId, ScheduleConstants.CANCELLED);
        if (task != null) {
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }

    /**
     * 删除redis中的数据
     *
     * @param task
     */
    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            cacheService.zRemove(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        }
    }

    private Task updateDb(long taskId, int status) {

        Task task = null;
        try {
            // 删除任务
            taskinfoMapper.deleteById(taskId);

            // 更新任务日志
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);
            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        } catch (Exception e) {
            log.error("task cancel error, taskId={}", taskId, e);
        }
        return task;

    }
}
