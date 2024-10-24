package com.shenzhen.dai.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shenzhen.dai.common.constant.ScheduleConstants;
import com.shenzhen.dai.common.redis.CacheService;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.model.schedule.Taskinfo;
import com.shenzhen.dai.model.schedule.TaskinfoLogs;
import com.shenzhen.dai.schedule.mapper.TaskinfoLogsMapper;
import com.shenzhen.dai.schedule.mapper.TaskinfoMapper;
import com.shenzhen.dai.schedule.service.TaskService;
import com.shuwei.dai.ObjectService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: daiyifan
 * @create: 2024-10-22 23:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TaskServiceImpl implements TaskService, ObjectService {
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

    @Override
    public Task pull(int type, int priority) {
        Task task = null;
        String key = type + "_" + priority;
        try {
            // 从redis中拉取数据
            String taskJson = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if (notBlank(taskJson)) {
                task = JSON.parseObject(taskJson, Task.class);
                // 修改数据库信息
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("task pull error", e);
        }
        return task;
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

    @Scheduled(cron = "0 */1 * * * *")
    void refresh() {
        // 在分布式系统中，一个方法在同一时间只能被一台机器的一个线程执行
        // trylock方法主要是使用redis的setnx的特性完成分布式锁的功能
        // A获取到锁以后，其他客户端不能操作，必须等获取到锁的客户端释放锁以后才能操作
        String lock = cacheService.tryLock("FUTURE_TASK_SYNC", 30 * 1000);
        if (notBlank(lock)) {

            log.info("未来数据刷新----定时任务");
            // 获取所有未来数据的集合key
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) {
                // 获取当前list数据的key
                String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];

                // 按照key和分值查询符合条件的数据
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                if (notEmpty(tasks)) {
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    log.info("成功刷新{}到{}", futureKey, topicKey);
                }
            }
        }
    }

    /**
     *
     */
    @PostConstruct
    @Scheduled(cron = "0 */5 * * * *")
    public void reloadData() {
        // 清理缓存中的数据
        clearCache();
        // 查询符合条件的任务：小于未来5分钟的数据
        // 获取5分钟后的时间毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();
        List<Taskinfo> taskinfos = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime, new Date(nextScheduleTime)));
        // 把这些数据添加到redis中
        if (notEmpty(taskinfos)) {
            for (Taskinfo taskinfo : taskinfos) {
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo, task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }
        log.info("数据库中的数据同步到了redis中");
    }

    public void clearCache() {
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        cacheService.delete(topicKeys);
        cacheService.delete(futureKeys);
    }
}
