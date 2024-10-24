package com.shenzhen.dai.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.shenzhen.dai.apis.schedule.IScheduleClient;
import com.shenzhen.dai.model.common.dtos.ResponseResult;
import com.shenzhen.dai.model.common.enums.TaskTypeEnum;
import com.shenzhen.dai.model.schedule.Task;
import com.shenzhen.dai.model.wemedia.pojos.WmNews;
import com.shenzhen.dai.wemedia.service.WmNewsAutoScanService;
import com.shenzhen.dai.wemedia.service.WmNewsTaskService;
import com.shuwei.dai.ProtostuffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description: 延迟任务
 * @author: daiyifan
 * @create: 2024-10-24 10:50
 */
@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Override
    @Async
    public void addNewTask(int id, Date publishTime) {
        log.info("----------------addNewTask begin");
        Task task = new Task();
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        task.setExecuteTime(publishTime.getTime());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        scheduleClient.addTask(task);
        log.info("----------------addNewTask end, id:{}, publishTime:{}", id, publishTime);
    }

    @Scheduled(fixedRate = 1000)
    public void scanNewsByTask() {
        log.info("消费任务,审核文章");
        ResponseResult<?> taskResp = scheduleClient.pull(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (taskResp.getCode().equals(200) && taskResp.getData() != null) {
            Task task = JSON.parseObject(JSON.toJSONString(taskResp.getData()), Task.class);
            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            log.info("在审核文章id:{},延时任务id:{}", wmNews.getId(), task.getTaskId());
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        }
    }
}
